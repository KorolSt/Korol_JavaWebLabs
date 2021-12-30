package com.example.hotel.controller.command.validation;

import com.example.hotel.model.dto.ApartmentDTO;
import com.example.hotel.model.entity.enums.ApartmentType;
import com.example.hotel.model.service.Sorter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.hotel.controller.Constants.REQ_PARAMETER_VAL_ASC_ORDER;

public class Validator {
    private static final String LOGIN_REGEXP = "^[^\\s$/()!&?%]{6,20}$";
    private static final String PASSWORD_REGEXP = "^[^\\s]{6,18}$";
    private static final String NAME_REGEXP = "^[a-zA-Zа-яА-ЯЁёІіЇї]{2,15}$";
    private static final String SURNAME_REGEXP = "^[a-zA-Zа-яА-ЯЁёІіЇї]{2,20}$";
    private static final String PHONE_REGEXP = "^[0-9-+\\s()]{10,20}$";
    private static final String EMAIL_REGEXP = "^[a-zA-Z._0-9]+?@([a-z]+?).([a-z]{1,3})$";

    private static final String ERR_MESSAGE_APT_TYPE_INVALID = "Apartment type is invalid!";

    private Validator() {
    }

    public static void loginAndPasswordValidate(String login, String password) throws InvalidInputException {
        if (login != null && login.matches(LOGIN_REGEXP) &&
                password != null && password.matches(PASSWORD_REGEXP)) {
            return;
        }
        throw new InvalidInputException("Login or password is invalid");
    }

    public static String[] nameAndSurnameValidate(String name, String surname) throws InvalidInputException {
        if (name != null && name.matches(NAME_REGEXP)
                && surname != null && surname.matches(SURNAME_REGEXP)) {
            return new String[]{name, surname};
        }
        throw new InvalidInputException("Name or surname is invalid");
    }

    public static String returnPhoneIfValid(String phone) throws InvalidInputException {
        if (phone != null && phone.matches(PHONE_REGEXP)) {
            return phone;
        }
        throw new InvalidInputException("Phone is invalid");
    }

    public static String returnEmailIfValid(String email) throws InvalidInputException {
        if (email != null && email.matches(EMAIL_REGEXP)) {
            return email;
        }
        throw new InvalidInputException("Email is invalid");
    }

    public static LocalDate[] dateInAndDateOutValidate(String dateIn, String dateOut, boolean dateInIsAfterNow) throws InvalidInputException {
        LocalDate in;
        LocalDate out;
        try {
            in = LocalDate.parse(dateIn);
            out = LocalDate.parse(dateOut);
        } catch (NullPointerException | DateTimeParseException e) {
            throw new InvalidInputException("'Date in' or 'Date out' is invalid!");
        }

        if (!in.isBefore(out)) {
            throw new InvalidInputException("'Date in' should be before 'Date out' !");
        }

        if (dateInIsAfterNow && !in.isAfter(LocalDate.now().minusDays(1))) {
            throw new InvalidInputException("'Date in' shouldn't be before today!");
        }

        return new LocalDate[]{in, out};
    }

    public static ApartmentType returnTypeIfValid(String apartmentType) throws InvalidInputException {
        try {
            if (apartmentType == null || apartmentType.equalsIgnoreCase("all")) {
                return null;
            }
            return ApartmentType.valueOf(apartmentType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(ERR_MESSAGE_APT_TYPE_INVALID);
        }
    }

    public static int returnPositiveIntIfValid(String integer) throws InvalidInputException {
        int res;
        try {
            res = Integer.parseInt(integer);
        } catch (NullPointerException | NumberFormatException e) {
            throw new InvalidInputException("Integer is invalid!");
        }
        if (res > 0) return res;
        throw new InvalidInputException("Integer is negative!");
    }

    public static List<ApartmentDTO.Status> returnStatusesIfValid(String[] statuses) throws InvalidInputException {
        List<ApartmentDTO.Status> apartmentStatuses = new ArrayList<>();
        try {
            if (statuses == null || statuses.length == 0) {
                return apartmentStatuses;
            }
            if (Arrays.stream(statuses).anyMatch(Objects::isNull)) {
                throw new InvalidInputException("Apartment type is null!");
            }
            Arrays.stream(statuses)
                    .map(s -> ApartmentDTO.Status.valueOf(s.toUpperCase()))
                    .forEach(apartmentStatuses::add);
            return apartmentStatuses;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Status is invalid!");
        }
    }

    public static List<Sorter> returnSortersIfValid(String[] sorters) throws InvalidInputException {
        List<Sorter> apartmentSorters = new ArrayList<>();
        try {
            if (sorters == null || sorters.length == 0) {
                return apartmentSorters;
            }
            if (Arrays.stream(sorters).anyMatch(Objects::isNull)) {
                throw new InvalidInputException("Sorter is null!");
            }
            Arrays.stream(sorters)
                    .map(s -> Sorter.valueOf(s.toUpperCase()))
                    .forEach(apartmentSorters::add);
            return apartmentSorters;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Sorter is invalid!");
        }
    }

    public static Sorter returnSorterIfValid(String sortBy, String order) throws InvalidInputException {
        if (sortBy == null) {
            return null;
        }
        String pSorter = sortBy.concat("_").concat(Objects.requireNonNullElse(order, REQ_PARAMETER_VAL_ASC_ORDER));
        try {
            return Sorter.valueOf(pSorter.toUpperCase());
        } catch (IllegalArgumentException ignore) {
            throw new InvalidInputException("Sorter is invalid!");
        }
    }
}
