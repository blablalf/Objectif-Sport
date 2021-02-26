package beetlejuice.objectifsport.model;

import java.util.UUID;

/**
 * The type Sport.
 */
public class Sport {

    private final String name; // Sport title
    private final UUID id; // To restore the good sport instances (stored in the goals) from shared instance we need to store id rather than a sport instance
    private final int authorizedGoals; // 0 for all goals | 1 for time goal | 2 for distance goal

    /**
     * Instantiates a new Sport.
     *
     * @param name the name
     */
    public Sport(String name) {
        this.name = name;
        authorizedGoals = 0;
        id = UUID.randomUUID();
    }

    /**
     * Instantiates a new Sport.
     *
     * @param name            the name
     * @param authorizedGoals the authorized goals
     */
    public Sport(String name, int authorizedGoals) {
        this.name = name;
        this.authorizedGoals = authorizedGoals;
        id = UUID.randomUUID();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets authorized goals.
     *
     * @return the authorized goals
     */
    public int getAuthorizedGoals() {
        return authorizedGoals;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }
}
