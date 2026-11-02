package logic.battlesystem;

import java.util.ArrayList;
import logic.cards.AbstractCard;
import logic.cards.PokemonCard;

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
        if (activePokemon == null || bank == null || replacement == null
                || !bank.contains(replacement)
                || activePokemon.getAttachedEnergy() < activePokemon.getRetreatCost()) {
            return false;
        }

        activePokemon.setAttachedEnergy(
                activePokemon.getAttachedEnergy() - activePokemon.getRetreatCost());
        int replacementIndex = bank.indexOf(replacement);
        bank.set(replacementIndex, activePokemon);
        activePokemon = replacement;
        return true;
    }

    public boolean attackOpponent(Player opponent, int baseDamage, int energyCost) {
        if (opponent == null || opponent == this || baseDamage < 0 || energyCost < 0
                || activePokemon == null || opponent.activePokemon == null
                || !canAttackThisTurn || hasAttacked
                || activePokemon.getAttachedEnergy() < energyCost) {
            return false;
        }

        PokemonCard defendingPokemon = opponent.activePokemon;
        defendingPokemon.receiveDamage(baseDamage);
        hasAttacked = true;

        if (defendingPokemon.isDefeated()) {
            awardPrizePoints(defendingPokemon.calculatePrizePoints());
            if (opponent.bank == null || opponent.bank.isEmpty()) {
                opponent.activePokemon = null;
            } else {
                opponent.activePokemon = opponent.bank.remove(0);
            }
        }
        return true;
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
                && evolution.getPhase() == currentPokemon.getPhase() + 1
                && evolution.getEvolvesFrom().equals(currentPokemon.getName());
    }
}
