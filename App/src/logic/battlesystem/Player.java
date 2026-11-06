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
        availableEnergy = 1;
        hasAttacked = false;
        canAttackThisTurn = canAttack;
        hasPlayedTrainer = false;
        activeRetreatCostReduced = false;
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

    public boolean placeOnBench(PokemonCard card) {
        if (card == null || !card.isBasicPokemon() || hand == null || bank == null
                || bank.size() >= 2 || !hand.getCards().contains(card)) {
            return false;
        }

        hand.removeCard(card);
        bank.add(card);
        return true;
    }

    public boolean attachEnergy(PokemonCard pokemon) {
        if (availableEnergy <= 0 || !isInPlay(pokemon)) {
            return false;
        }

        pokemon.setAttachedEnergy(pokemon.getAttachedEnergy() + 1);
        availableEnergy--;
        return true;
    }

    public boolean retreatActive() {
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        return retreatActive(bank.get(0));
    }

    public boolean retreatActive(PokemonCard replacement) {
        int retreatCost = getActiveRetreatCost();
        if (activePokemon == null || bank == null || replacement == null
                || !bank.contains(replacement)
                || activePokemon.getAttachedEnergy() < retreatCost) {
            return false;
        }

        activePokemon.setAttachedEnergy(
                activePokemon.getAttachedEnergy() - retreatCost);
        int replacementIndex = bank.indexOf(replacement);
        bank.set(replacementIndex, activePokemon);
        activePokemon = replacement;
        return true;
    }

    public boolean attackOpponent(Player opponent, int baseDamage, int energyCost) {
        Type attackType = activePokemon == null ? null : activePokemon.getType();
        return performAttack(opponent, baseDamage, energyCost, attackType);
    }

    private boolean performAttack(Player opponent, int baseDamage, int energyCost, Type attackType) {
        if (opponent == null || opponent == this || baseDamage < 0 || energyCost < 0
                || activePokemon == null || opponent.activePokemon == null
                || !canAttackThisTurn || hasAttacked
                || activePokemon.getAttachedEnergy() < energyCost) {
            return false;
        }

        PokemonCard defendingPokemon = opponent.activePokemon;
        defendingPokemon.receiveDamage(baseDamage, attackType);
        hasAttacked = true;

        opponent.handleActivePokemonDefeat();
        return true;
    }

    public boolean attackOpponent(Player opponent, Attack attack) {
        if (attack == null || activePokemon == null
                || !activePokemon.getAttacks().contains(attack)) {
            return false;
        }
        return performAttack(
                opponent, attack.getBaseDamage(), attack.getEnergyCost(), attack.getEnergyType());
    }

    private void handleActivePokemonDefeat() {
        if (activePokemon == null || !activePokemon.isDefeated()) {
            return;
        }
        awardPrizePoints(activePokemon.calculatePrizePoints());
        if (bank == null || bank.isEmpty()) {
            activePokemon = null;
        } else {
            activePokemon = bank.remove(0);
        }
    }

    
    public boolean playPlayableCard(AbstractCard card, Game game, PokemonCard target) {
         if (!(card instanceof Effect)) {
            return false;
        }
         Effect cardWithEffect = (Effect) card;
         if (hand == null || !hand.getCards().contains(card)) {
             return false;
         }
        boolean isTrainerCard = card instanceof TrainerCard;
        if (isTrainerCard && hasPlayedTrainer) {
            return false;
        }

        hand.removeCard(card);
        boolean activated = cardWithEffect.activateEffect(game, this, target);
        if (!activated) {
            hand.addCard(card);
            return false;
        }
        if (isTrainerCard) {
            hasPlayedTrainer = true;
        }
        return true;
    }

    public boolean hasPlayedTrainer() {
        return hasPlayedTrainer;
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

    public int attachWaterEnergy(PokemonCard target, int maximum) {
        if (target == null || maximum <= 0 || !isInPlay(target) || target.getType() != Type.WATER) {
            return 0;
        }
        target.setAttachedEnergy(target.getAttachedEnergy() + maximum);
        return maximum;
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

    public int getActiveRetreatCost() {
        if (activePokemon == null) {
            return 0;
        }
        int reduction = activeRetreatCostReduced ? 1 : 0;
        return Math.max(0, activePokemon.getRetreatCost() - reduction);
    }

    private boolean isInPlay(PokemonCard pokemon) {
        return pokemon != null
                && (pokemon == activePokemon || (bank != null && bank.contains(pokemon)));
    }

    private void awardPrizePoints(int points) {
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

        int damageTaken = target.getMaxHealthPoints() - target.getHealthPoints();
        evolution.receiveDamage(damageTaken);

        if (target == activePokemon) {
            activePokemon = evolution;
        } else {
            int index = bank.indexOf(target);
            bank.set(index, evolution);
        }

        hand.removeCard(evolution);
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
                && evolution.getEvolvesFrom() != null
                && evolution.getPhase() == currentPokemon.getPhase() + 1
                && evolution.getEvolvesFrom().equals(currentPokemon.getName());
    }
}
