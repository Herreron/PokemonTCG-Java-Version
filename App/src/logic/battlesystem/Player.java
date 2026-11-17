package logic.battlesystem;

import java.util.ArrayList;
import logic.cards.AbstractCard;
import logic.cards.Attack;
import logic.cards.Effect;
import logic.cards.PokemonCard;
import logic.cards.TrainerCard;
import logic.cards.Type;

public class Player {

    private String name;
    private Deck deck;
    private Hand hand;
    private ArrayList<PokemonCard> bank;
    private PokemonCard activePokemon;
    private byte prizePoints;
    private byte availableEnergy;
    private boolean hasAttacked;
    private boolean canAttackThisTurn;
    private boolean hasPlayedTrainer;
    private boolean activeRetreatCostReduced;
    private byte turnsStarted;
    private ArrayList<PokemonCard> pokemonEligibleToEvolve;

    public Player(String name, Deck deck, Hand hand, ArrayList<PokemonCard> bank, PokemonCard activePokemon,
            byte prizePoints, byte availableEnergy) {
        this.name = name;
        this.deck = deck;
        this.hand = hand;
        if (bank == null) {
            this.bank = new ArrayList<>();
        } else {
            this.bank = bank;
        }
        this.activePokemon = activePokemon;
        this.prizePoints = prizePoints;
        this.availableEnergy = availableEnergy;
        this.pokemonEligibleToEvolve = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public ArrayList<PokemonCard> getBank() {
        return bank;
    }

    public void setBank(ArrayList<PokemonCard> bank) {
        if (bank != null) {
            this.bank = bank;
        }
    }

    public PokemonCard getActivePokemon() {
        return activePokemon;
    }

    public void setActivePokemon(PokemonCard activePokemon) {
        this.activePokemon = activePokemon;
    }

    public byte getPrizePoints() {
        return prizePoints;
    }

    public void setPrizePoints(byte prizePoints) {
        this.prizePoints = prizePoints;
    }

    public byte getAvailableEnergy() {
        return availableEnergy;
    }

    public void setAvailableEnergy(byte availableEnergy) {
        this.availableEnergy = availableEnergy;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public boolean selectInitialActivePokemon(PokemonCard pokemon) {
        if (activePokemon != null || pokemon == null || !pokemon.isBasicPokemon()
                || hand == null || !hand.getCards().contains(pokemon)) {
            return false;
        }

        hand.removeCard(pokemon);
        activePokemon = pokemon;
        return true;
    }

    public void prepareTurn(boolean canAttack) {
        if (turnsStarted < 2) {
            turnsStarted++;
        }
        availableEnergy = 1;
        hasAttacked = false;
        canAttackThisTurn = canAttack;
        hasPlayedTrainer = false;
        activeRetreatCostReduced = false;

        pokemonEligibleToEvolve.clear();
        if (turnsStarted > 1) {
            if (activePokemon != null) {
                pokemonEligibleToEvolve.add(activePokemon);
            }
            if (bank != null) {
                pokemonEligibleToEvolve.addAll(bank);
            }
        }
    }

    public boolean drawCard() {
        if (deck == null || hand == null) {
            return false;
        }

        AbstractCard drawnCard = deck.drawCard();
        if (drawnCard == null) {
            return false;
        }

        hand.addCard(drawnCard);
        return true;
    }

    // Devuelve por qué no se puede poner la carta en la banca, o null si sí se puede
    public String getPlaceOnBenchError(PokemonCard card) {
        if (card == null || hand == null || !hand.getCards().contains(card)) {
            return "Esa carta no está en tu mano.";
        }
        if (!card.isBasicPokemon()) {
            return card.getName() + " es una evolución: solo entra en juego evolucionando a un Pokémon.";
        }
        if (bank == null || bank.size() >= 2) {
            return "Tu banca ya está llena (máximo 2 Pokémon).";
        }
        return null;
    }

    public boolean placeOnBench(PokemonCard card) {
        if (getPlaceOnBenchError(card) != null) {
            return false;
        }

        hand.removeCard(card);
        bank.add(card);
        return true;
    }

    // Devuelve por qué no se puede adjuntar energía, o null si sí se puede
    public String getAttachEnergyError(PokemonCard pokemon) {
        if (availableEnergy <= 0) {
            return "Ya usaste la energía de la zona en este turno.";
        }
        if (!isInPlay(pokemon)) {
            return "Solo puedes adjuntar energía a tus Pokémon en juego.";
        }
        return null;
    }

    public boolean attachEnergy(PokemonCard pokemon) {
        if (getAttachEnergyError(pokemon) != null) {
            return false;
        }

        pokemon.setAttachedEnergy((byte) Math.min(Byte.MAX_VALUE, pokemon.getAttachedEnergy() + 1));
        availableEnergy--;
        return true;
    }

    public boolean retreatActive() {
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        return retreatActive(bank.get(0));
    }

    // Devuelve por qué no se puede retirar el activo, o null si sí se puede
    public String getRetreatError(PokemonCard replacement) {
        if (activePokemon == null) {
            return "No tienes un Pokémon activo.";
        }
        if (bank == null || bank.isEmpty()) {
            return "No tienes Pokémon en la banca para reemplazar a " + activePokemon.getName() + ".";
        }
        if (replacement == null || !bank.contains(replacement)) {
            return "Elige uno de tus Pokémon de la banca.";
        }
        byte retreatCost = getActiveRetreatCost();
        if (activePokemon.getAttachedEnergy() < retreatCost) {
            return activePokemon.getName() + " necesita " + retreatCost + " energía(s) para retirarse.";
        }
        return null;
    }

    public boolean retreatActive(PokemonCard replacement) {
        byte retreatCost = getActiveRetreatCost();
        if (getRetreatError(replacement) != null) {
            return false;
        }

        activePokemon.setAttachedEnergy(
                (byte) Math.max(0, activePokemon.getAttachedEnergy() - retreatCost));
        int replacementIndex = bank.indexOf(replacement);
        bank.set(replacementIndex, activePokemon);
        activePokemon = replacement;
        return true;
    }

    public boolean attackOpponent(Player opponent, short baseDamage, byte energyCost) {
        // Esta variante no recibe un ataque concreto; se trata como incoloro.
        return performAttack(opponent, baseDamage, energyCost, Type.COLORLESS);
    }

    private boolean performAttack(Player opponent, short baseDamage, byte energyCost, Type attackType) {
        if (opponent == null || opponent == this || baseDamage < 0 || energyCost < 0
                || activePokemon == null || opponent.activePokemon == null
                || !canAttackThisTurn || hasAttacked
                || activePokemon.getAttachedEnergy() < energyCost) {
            return false;
        }

        PokemonCard defendingPokemon = opponent.activePokemon;
        defendingPokemon.receiveDamage(baseDamage, attackType);
        hasAttacked = true;

        byte prizePoints = opponent.handleActivePokemonDefeat();
        if (prizePoints > 0) {
            awardPrizePoints(prizePoints);
        }
        return true;
    }

    // Devuelve por qué no se puede usar el ataque, o null si sí se puede
    public String getAttackError(Player opponent, Attack attack) {
        if (activePokemon == null) {
            return "No tienes un Pokémon activo para atacar.";
        }
        if (opponent == null || opponent == this || opponent.activePokemon == null) {
            return "El rival no tiene un Pokémon activo.";
        }
        if (!canAttackThisTurn) {
            return "El primer jugador no puede atacar en su primer turno.";
        }
        if (hasAttacked) {
            return "Ya atacaste en este turno.";
        }
        if (attack == null || !activePokemon.getAttacks().contains(attack)) {
            return "Ese ataque no es de tu Pokémon activo.";
        }
        if (activePokemon.getAttachedEnergy() < attack.getEnergyCost()) {
            return activePokemon.getName() + " no tiene la energía necesaria para " + attack.getName() + ".";
        }
        return null;
    }

    // Calcula el daño que haría el ataque: daño base + 20 si el defensor es débil al tipo del ataque
    public int getAttackDamage(Player opponent, Attack attack) {
        if (opponent == null || opponent.activePokemon == null || attack == null) {
            return 0;
        }
        int damage = attack.getBaseDamage();
        if (opponent.activePokemon.getType().isWeakTo(attack.getEnergyType())) {
            damage += Type.WEAKNESS_BONUS;
        }
        return damage;
    }

    public boolean attackOpponent(Player opponent, Attack attack) {
        if (getAttackError(opponent, attack) != null) {
            return false;
        }
        return performAttack(
                opponent, attack.getBaseDamage(), attack.getEnergyCost(), attack.getEnergyType());
    }
    
    private byte handleActivePokemonDefeat() {
        if (activePokemon == null || !activePokemon.isDefeated()) {
            return (byte) 0;
        }
        byte prizePoints = activePokemon.calculatePrizePoints();
        activePokemon = null;
        return prizePoints;
    }

    public boolean promotePokemon(PokemonCard pokemon) {
        if (activePokemon != null || bank == null || pokemon == null) {
            return false;
        }

        int pokemonIndex = bank.indexOf(pokemon);
        if (pokemonIndex < 0) {
            return false;
        }

        activePokemon = bank.remove(pokemonIndex);
        return true;
    }

    
    public boolean playPlayableCard(AbstractCard card, Game game, PokemonCard target) {
         if (!(card instanceof Effect)) {
            return false;
        }
         Effect cardWithEffect = (Effect) card;
         if (hand == null || !hand.getCards().contains(card)) {
             return false;
         }
        boolean isSupportCard = card instanceof TrainerCard
                && ((TrainerCard) card).isSupport();
        if (isSupportCard && hasPlayedTrainer) {
            return false;
        }

        hand.removeCard(card);
        boolean activated = cardWithEffect.activateEffect(game, this, target);
        if (!activated) {
            hand.addCard(card);
            return false;
        }
        if (isSupportCard) {
            hasPlayedTrainer = true;
        }
        return true;
    }

    public boolean hasPlayedTrainer() {
        return hasPlayedTrainer;
    }

    // Devuelve por qué no se puede jugar la carta de entrenador, o null si sí se puede
    public String getPlayCardError(AbstractCard card) {
        if (!(card instanceof Effect) || hand == null || !hand.getCards().contains(card)) {
            return "Esa carta no se puede jugar.";
        }
        boolean isSupportCard = card instanceof TrainerCard && ((TrainerCard) card).isSupport();
        if (isSupportCard && hasPlayedTrainer) {
            return "Solo puedes jugar una carta de soporte por turno.";
        }
        return null;
    }

    public boolean useProfessorResearch() {
        if (hand == null) {
            return false;
        }
        hand.discardAll();
        drawCard();
        drawCard();
        return true;
    }

    public byte attachWaterEnergy(PokemonCard target, byte maximum) {
        if (target == null || maximum <= 0 || !isInPlay(target) || target.getType() != Type.WATER) {
            return (byte) 0;
        }
        target.setAttachedEnergy((byte) Math.min(
                Byte.MAX_VALUE, target.getAttachedEnergy() + maximum));
        return maximum;
    }

    public boolean healPokemon(PokemonCard pokemon, short amount) {
        if (amount <= 0 || !isInPlay(pokemon)
                || pokemon.getHealthPoints() >= pokemon.getMaxHealthPoints()) {
            return false;
        }
        pokemon.heal(amount);
        return true;
    }

    public boolean searchBasicPokemonInDeck() {
        if (deck == null || hand == null) {
            return false;
        }
        PokemonCard pokemon = deck.drawBasicPokemon();
        if (pokemon == null) {
            return false;
        }
        hand.addCard(pokemon);
        return true;
    }

    public boolean reduceActiveRetreatCost() {
        if (activePokemon == null || activeRetreatCostReduced) {
            return false;
        }
        activeRetreatCostReduced = true;
        return true;
    }

    public byte getActiveRetreatCost() {
        if (activePokemon == null) {
            return (byte) 0;
        }
        byte reduction = activeRetreatCostReduced ? (byte) 1 : (byte) 0;
        return (byte) Math.max(0, activePokemon.getRetreatCost() - reduction);
    }

    private boolean isInPlay(PokemonCard pokemon) {
        return pokemon != null
                && (pokemon == activePokemon || (bank != null && bank.contains(pokemon)));
    }

    private void awardPrizePoints(byte points) {
        int updatedPoints = prizePoints + points;
        if (updatedPoints > Byte.MAX_VALUE) {
            updatedPoints = Byte.MAX_VALUE;
        }
        prizePoints = (byte) updatedPoints;
    }

    private boolean isValidEvolved(PokemonCard evolution) {
        return evolution != null
                && !evolution.isBasicPokemon()
                && hand != null
                && hand.getCards().contains(evolution);
    }

    private PokemonCard findEligiblePokemonToEvolve(PokemonCard evolution) {
        if (activePokemon != null && canEvolve(activePokemon, evolution)) {
            return activePokemon;
        }

        if (bank != null) {
            for (PokemonCard pokemon : bank) {
                if (canEvolve(pokemon, evolution)) {
                    return pokemon;
                }
            }
        }
        return null;
    }

    private void executeEvolution(PokemonCard target, PokemonCard evolution) {
        evolution.setAttachedEnergy(target.getAttachedEnergy());

        short damageTaken = (short) (target.getMaxHealthPoints() - target.getHealthPoints());
        evolution.receiveDamage(damageTaken);

        if (target == activePokemon) {
            activePokemon = evolution;
        } else {
            int index = bank.indexOf(target);
            bank.set(index, evolution);
        }

        hand.removeCard(evolution);
    }

    // Devuelve por qué no se puede evolucionar ese Pokémon con esa carta, o null si sí se puede
    public String getEvolveError(PokemonCard evolution, PokemonCard target) {
        if (!isValidEvolved(evolution)) {
            return "Esa carta no es una evolución de tu mano.";
        }
        if (!isInPlay(target)) {
            return "Elige uno de tus Pokémon en juego.";
        }
        if (turnsStarted <= 1) {
            return "No puedes evolucionar en tu primer turno.";
        }
        if (evolution.getEvolvesFrom() == null || !evolution.getEvolvesFrom().equals(target.getName())
                || evolution.getPhase() != target.getPhase() + 1) {
            return evolution.getName() + " no evoluciona de " + target.getName() + ".";
        }
        if (!pokemonEligibleToEvolve.contains(target)) {
            return target.getName() + " entró en juego este turno o ya evolucionó en este turno.";
        }
        return null;
    }

    // Evoluciona un Pokémon concreto (el que el jugador eligió en el tablero)
    public boolean evolvePokemon(PokemonCard evolution, PokemonCard target) {
        if (getEvolveError(evolution, target) != null) {
            return false;
        }
        executeEvolution(target, evolution);
        return true;
    }

    public boolean evolvePokemon(PokemonCard evolution) {
        if (!isValidEvolved(evolution)) {
            return false;
        }

        PokemonCard targetPokemon = findEligiblePokemonToEvolve(evolution);
        if (targetPokemon == null) {
            return false;
        }

        executeEvolution(targetPokemon, evolution);
        return true;
    }

    private boolean canEvolve(PokemonCard currentPokemon, PokemonCard evolution) {
        return currentPokemon != null
                && pokemonEligibleToEvolve.contains(currentPokemon)
                && evolution.getEvolvesFrom() != null
                && evolution.getPhase() == currentPokemon.getPhase() + 1
                && evolution.getEvolvesFrom().equals(currentPokemon.getName());
    }
}
