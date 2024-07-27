package org.proom.engine.hand;

/**
 * @author vasyalike
 */
enum HandState {
    PRE_FLOP, FLOP, TURN, RIVER, FINISHED;
    static HandState next(HandState state) {
        return switch (state) {
            case null -> PRE_FLOP;
            case PRE_FLOP -> FLOP;
            case FLOP -> TURN;
            case TURN -> RIVER;
            case RIVER -> FINISHED;
            case FINISHED -> throw new IllegalStateException();
        };
    }
}
