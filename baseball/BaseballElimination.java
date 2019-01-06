/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BaseballElimination {
    private int teamNumbers;
    private List<String> teamNames;
    private int[] wins;
    private int[] losses;
    private int[] remains;
    private int[][] games;
    private List<String> R;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        teamNumbers = in.readInt();
        teamNames = new ArrayList<>();
        wins = new int[teamNumbers];
        losses = new int[teamNumbers];
        remains = new int[teamNumbers];
        games = new int[teamNumbers][teamNumbers];
        for (int i = 0; i < teamNumbers; i++) {
            teamNames.add(in.readString());
            wins[i] = Integer.parseInt(in.readString());
            losses[i] = Integer.parseInt(in.readString());
            remains[i] = Integer.parseInt(in.readString());
            for (int j = 0; j < teamNumbers; j++) {
                games[i][j] = Integer.parseInt(in.readString());
            }
        }
    }

    // find the team's index by it's name
    private int getTeamIndex(String teamName) {
        if (!teamNames.contains(teamName)) throw new IllegalArgumentException();
        return teamNames.indexOf(teamName);
    }


    // number of teams
    public int numberOfTeams() {
        return teamNumbers;
    }

    // all teams
    public Iterable<String> teams() {
        List<String> copyTeams = new ArrayList<>(teamNames);
        return copyTeams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) throw new IllegalArgumentException();
        return wins[getTeamIndex(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) throw new IllegalArgumentException();
        return losses[getTeamIndex(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) throw new IllegalArgumentException();
        return remains[getTeamIndex(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) throw new IllegalArgumentException();
        return games[getTeamIndex(team1)][getTeamIndex(team2)];
    }

    // s = 0, t = 1, team continue then followed by games.
    // return the vertex of game in the network
    private int getGameVertex(int i, int j) {
        return 2 + teamNumbers + teamNumbers * i + j;
    }

    // return the vertex of team in the network
    private int getTeamVertex(int i) {
        return 2 + i;
    }

    private boolean simpleEliminated(int teamId) {
        for (int i = 0; i < this.teamNumbers; i++) {
            if (i == teamId) continue;
            if (wins[teamId] + remains[teamId] < wins[i]) {
                this.R = new ArrayList<>();
                this.R.add(teamNames.get(i));
                return true;
            }
        }
        return false;
    }


    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) throw new IllegalArgumentException();
        int teamId = getTeamIndex(team);
        if (simpleEliminated(teamId)) return true;
        FlowNetwork network = new FlowNetwork(2 + teamNumbers + teamNumbers * teamNumbers);
        // s = 0, t = 1, team continue then followed by games.
        // link s to games except the game teamId take part in.
        // link games to the teams.
        // link teams to t vertex.
        for (int i = 0; i < teamNumbers; i++) {
            if (i == teamId) continue;
            FlowEdge toT = new FlowEdge(getTeamVertex(i), 1,
                                        wins[teamId] + remains[teamId] - wins[i]);
            network.addEdge(toT);
            for (int j = 0; j < i; j++) {
                if (j == teamId) continue;
                FlowEdge toGame = new FlowEdge(0, getGameVertex(i, j), games[i][j]);
                FlowEdge toTeam0 = new FlowEdge(getGameVertex(i, j), getTeamVertex(i),
                                                Integer.MAX_VALUE);
                FlowEdge toTeam1 = new FlowEdge(getGameVertex(i, j), getTeamVertex(j),
                                                Integer.MAX_VALUE);
                network.addEdge(toGame);
                network.addEdge(toTeam0);
                network.addEdge(toTeam1);
            }
        }
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, 1);
        // 0 is s. if any path not full. the team is eliminated,
        for (FlowEdge flowEdge : network.adj(0)) {
            if (flowEdge.residualCapacityTo(flowEdge.other(0)) != 0) {
                this.R = new ArrayList<>();
                for (int i = 0; i < teamNumbers; i++) {
                    if (fordFulkerson.inCut(getTeamVertex(i))) {
                        R.add(teamNames.get(i));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) throw new IllegalArgumentException();
        if (isEliminated(team)) {
            return this.R;
        }
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
