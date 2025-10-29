package Hero;
import MovingType.*;

public class Hero {
    private MovingType movingType = new OnFootMove();

    public void setMoving(MovingType movingType) {
        if (movingType != null){
            this.movingType = movingType;
        }
    }

    public void move() {
        movingType.move();
    }
}
