package clock

import java.time.Instant

class RealClock : Clock {
    override fun now(): Instant = Instant.now()
}
