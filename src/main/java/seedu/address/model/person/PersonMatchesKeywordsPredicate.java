package seedu.address.model.person;

import java.util.Arrays;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person} matches any of the keywords given.
 */
public class PersonMatchesKeywordsPredicate implements Predicate<Person> {
    private final String keywords;

    public PersonMatchesKeywordsPredicate(String keywords) {
        this.keywords = keywords;
    }

    // This method is used to compute the Levenshtein distance, which
    // how different two strings are from one another by counting the minimum
    // number of operations (insertions, deletions and substitutions of characters)
    // required to transform one string to another.
    private int getLevenshteindist(String str1, String str2) {

        // A 2-D matrix to store previously calculated
        // answers of subproblems
        int[][] arr = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {

                // If str1 is empty, the only possible
                // method of conversion with minimum operations
                // would be for all characters of
                // str2 to be inserted into str1,
                if (i == 0) {
                    arr[i][j] = j;
                }

                // If str2 is empty the only possible
                // method of conversion with minimum operations
                // would be for all characters of str1
                // to be removed.
                else if (j == 0) {
                    arr[i][j] = i;
                }

                else {
                    //else find minimum among the three operations below
                    arr[i][j] = minEdits(arr[i - 1][j - 1]
                                    + numOfReplacement(str1.charAt(i - 1), str2.charAt(j - 1)),
                            arr[i - 1][j] + 1,
                            arr[i][j - 1] + 1);
                }
            }
        }
        return arr[str1.length()][str2.length()];
    }

    // This method uses the levenshtein distance to calculate the similarity
    // between two strings in the range [0, 1].
    private double findSimilarity(String x, String y) {
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            return (maxLength - getLevenshteindist(x, y)) / maxLength;
        }
        return 1.0;
    }

    // This method is used to check for distinct charecters in str1 and str2.
    static int numOfReplacement(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    // This method is used to receive the count of different
    // operations performed and return the
    // minimum value among them.
    static int minEdits(int... nums) {
        return Arrays.stream(nums).min().orElse(Integer.MAX_VALUE);
    }

    private boolean matchesName(Person person) {
        return findSimilarity(keywords, person.getName().fullName) > 0.5
                || StringUtil.containsWordIgnoreCase(
                        person.getName().fullName, keywords);
    }

    private boolean matchesAddress(Person person) {
        if (person.getAddress().isPresent()) {
            return findSimilarity(keywords, String.valueOf(person.getAddress().get().value)) > 0.5
                    || StringUtil.containsWordIgnoreCase(
                            String.valueOf(person.getAddress().get().value), keywords);
        } else {
            return false;
        }
    }

    private boolean matchesRole(Person person) {
        if (person.getRole().isPresent()) {
            return findSimilarity(keywords, String.valueOf(person.getRole().get().role)) > 0.5
                    || StringUtil.containsWordIgnoreCase(
                            String.valueOf(person.getRole().get().role), keywords);
        } else {
            return false;
        }
    }

    private boolean matchesGitHubUser(Person person) {
        return findSimilarity(keywords, person.getGitHubUser().toString()) > 0.5
                || StringUtil.containsWordIgnoreCase(person.getGitHubUser().toString(), keywords);
    }

    private boolean matchesTags(Person person) {
        Object[] tags = person.getTags().toArray();
        for (int i = 0; i < tags.length; i++) {
            if (findSimilarity(keywords, tags[i].toString()) > 0.5
                    || StringUtil.containsWordIgnoreCase(tags[i].toString(), keywords)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean test(Person person) {
        if (matchesName(person)) {
            return true;
        } else if (matchesAddress(person)) {
            return true;
        } else if (matchesRole(person)) {
            return true;
        } else if (matchesTags(person)) {
            return true;
        } else if (matchesGitHubUser(person)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonMatchesKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((PersonMatchesKeywordsPredicate) other).keywords)); // state check
    }
}
