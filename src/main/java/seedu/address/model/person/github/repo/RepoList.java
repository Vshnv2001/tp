package seedu.address.model.person.github.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for all the repositories of the class.
 */
public class RepoList {
    private final List<Repo> repoList;

    /**
     * Constructor for RepoList Class.
     */
    public RepoList() {
        this.repoList = new ArrayList<>();
    }

    public RepoList(List<Repo> repoList) {
        this.repoList = repoList;
    }

    public List<Repo> getRepos() {
        return Collections.unmodifiableList(this.repoList);
    }

    public int getNumberOfRepos() {
        return repoList.size();
    }

    /**
     * Method to add new Repo to Existing RepoList.
     *
     * @param nextRepo the next Repo Object to be added.
     */
    public void add(Repo nextRepo) {
        this.repoList.add(nextRepo);
    }

    /**
     * Empties all the repositories in the given repository list.
     *
     * @return Same RepoList after clearing out all the repositories.
     */
    public RepoList clear() {
        this.repoList.clear();
        return this;
    }

}