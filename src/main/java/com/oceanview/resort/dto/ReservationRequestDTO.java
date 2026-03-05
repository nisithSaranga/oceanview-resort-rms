package com.oceanview.resort.dto;

import com.oceanview.resort.enums.RoomType;

import java.time.LocalDate;
import java.util.Objects;

public final class ReservationRequestDTO {

    private final String fullName;
    private final String address;
    private final String contactNumber;
    private final RoomType roomType;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    private ReservationRequestDTO(Builder builder) {
        this.fullName = builder.fullName;
        this.address = builder.address;
        this.contactNumber = builder.contactNumber;
        this.roomType = builder.roomType;
        this.checkIn = builder.checkIn;
        this.checkOut = builder.checkOut;
    }

    /**
     * Convenience factory to start builder chain:
     * ReservationRequestDTO.builder().withFullName(...).build();
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public static final class Builder {
        private String fullName;
        private String address;
        private String contactNumber;
        private RoomType roomType;
        private LocalDate checkIn;
        private LocalDate checkOut;

        // Public constructor allows lecturer-style usage too:
        // new ReservationRequestDTO.Builder()...
        public Builder() {
        }

        public Builder withFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public Builder withRoomType(RoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder withCheckIn(LocalDate checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public Builder withCheckOut(LocalDate checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public ReservationRequestDTO build() {
            validateAndNormalize();
            return new ReservationRequestDTO(this);
        }

        private void validateAndNormalize() {
            this.fullName = normalizeRequired(this.fullName, "fullName");
            this.address = normalizeRequired(this.address, "address");
            this.contactNumber = normalizeRequired(this.contactNumber, "contactNumber");

            Objects.requireNonNull(this.roomType, "roomType is required");
            Objects.requireNonNull(this.checkIn, "checkIn is required");
            Objects.requireNonNull(this.checkOut, "checkOut is required");

            if (!this.checkOut.isAfter(this.checkIn)) {
                throw new IllegalStateException("checkOut must be after checkIn");
            }
        }

        private String normalizeRequired(String value, String fieldName) {
            if (value == null) {
                throw new IllegalStateException(fieldName + " is required");
            }

            String trimmed = value.trim();
            if (trimmed.isEmpty()) {
                throw new IllegalStateException(fieldName + " must not be blank");
            }

            return trimmed;
        }
    }
}

