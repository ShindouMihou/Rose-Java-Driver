package pw.mihou.rosedb.enums;

public enum Listening {

    OPEN(0), CLOSE(1), RECEIVE(2);

    public int id;

    Listening(int id){
        this.id = id;
    }
}
