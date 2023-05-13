package model;


public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;
    private String address;

    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private final int originRank;

    public int getOriginRank() {
        return originRank;
    }

    private PlayerColor winner = null;


    public ChessPiece(PlayerColor owner, String name, int rank, String address) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
        this.address = address;
        this.originRank = rank;
    }

    public boolean canCapture(ChessPiece target) {
        if (target == null) {
            return false;
        }

        // TODO: Finish this method!
        if (this.rank >= target.rank && (this.rank != 8 || target.rank != 1)) {
            return true;
        }
        if (this.rank == 1 && target.rank == 8) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }

    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "ChessPiece{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                ", rank=" + rank +
                '}';
    }
}



