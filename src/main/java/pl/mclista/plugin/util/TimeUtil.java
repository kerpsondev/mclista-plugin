package pl.mclista.plugin.util;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class TimeUtil {

  private TimeUtil() {}

  public final static ZoneId ZONE_ID = ZoneId.of("Europe/Warsaw");

  private static final ChronoUnit[] UNITS = new ChronoUnit[]{
      ChronoUnit.YEARS,
      ChronoUnit.MONTHS,
      ChronoUnit.WEEKS,
      ChronoUnit.DAYS,
      ChronoUnit.HOURS,
      ChronoUnit.MINUTES,
      ChronoUnit.SECONDS
  };

  public static String format(Instant time) {
    return TimeUtil.format(time.toEpochMilli());
  }

  public static String format(Duration duration) {
    return TimeUtil.format(Instant.now().plus(duration));
  }

  public static String format(int seconds) {
    StringBuilder builder = new StringBuilder();
    int outputSize = 0;

    for (ChronoUnit unit : UNITS) {
      long n = seconds / unit.getDuration().getSeconds();
      if (n > 0) {
        seconds -= (int) (unit.getDuration().getSeconds() * n);
        if (outputSize != 0) {
          builder.append(" ");
        }

        builder.append(formatPart(n, unit));
        outputSize++;
      }
      if (seconds <= 0 || outputSize >= Integer.MAX_VALUE) {
        break;
      }
    }

    if (outputSize == 0) {
      return formatPart(0, ChronoUnit.SECONDS);
    }

    return builder.toString();
  }

  public static String format(long duration) {
    if (duration == 0) {
      return "0sek";
    }

    duration = (duration - System.currentTimeMillis());
    if (duration < 1000) {
      return duration + "ms";
    }

    int seconds = (int) (duration / 1000L);
    return format(seconds);
  }

  private static String formatPart(long amount, ChronoUnit unit) {
    StringBuilder formatted = new StringBuilder();
    formatted.append(amount).append(" ");

    switch (unit) {
      case YEARS:
        if (amount <= 0 || amount >= 5) {
          formatted.append("lat");
        }
        if (amount == 1) {
          formatted.append("rok");
        }
        if (amount >= 2 && amount <= 4) {
          formatted.append("lata");
        }

        break;
      case MONTHS:
        formatted.append("mies.");
        break;
      case WEEKS:
        formatted.append("tyg.");
        break;
      case DAYS:
        if (amount == 1) {
          formatted.append("dzień");
        } else {
          formatted.append("dni");
        }

        break;
      case HOURS:
        formatted.append("godz.");
        break;
      case MINUTES:
        formatted.append("min.");
        break;
      case SECONDS:
        formatted.append("sek.");
        break;
    }

    return formatted.toString();
  }
}