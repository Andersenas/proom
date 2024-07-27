package org.proom.engine.hand;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author vasyalike
 */
public record PotWinners(BigDecimal pot, List<Player> players) { }
