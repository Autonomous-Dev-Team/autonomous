package org.robotics.car.exception;

/**
     * Exception thrown when timeout occurs
     */
    public class TimeoutException extends Exception {

        private final String reason;
        
        public TimeoutException( String reason ) {
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return this.reason;
        }
    }
