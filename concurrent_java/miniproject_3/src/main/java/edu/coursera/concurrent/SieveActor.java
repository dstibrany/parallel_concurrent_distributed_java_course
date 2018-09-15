package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;
import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determine the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        int numPrimes = 0;
        SieveActorActor actor = new SieveActorActor(2);
        finish(() -> {
            for (int i = 2; i <= limit; i++) {
                actor.send(i);
            }
        });

        SieveActorActor loopActor = actor;
        while (loopActor != null) {
            numPrimes++;
            loopActor = loopActor.nextActor();
        }

        return numPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        private final int multiple;
        private SieveActorActor nextActor = null;

        public SieveActorActor(final int multiple) {
            this.multiple = multiple;
        }
        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            int i = (int) msg;
            if (i % multiple != 0) {
                if (nextActor == null) {
                    nextActor = new SieveActorActor(i);
                }
                nextActor.send(msg);
            }
        }

        private SieveActorActor nextActor() {
            return nextActor;
        }
    }
}
