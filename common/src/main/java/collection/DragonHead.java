package collection;
import java.io.Serializable;

public class DragonHead implements Serializable {
    /**
     * размер головы объекта класса. Поле может быть null.
     */
    private Double size = null;


    public DragonHead(Double size) {
        this.size = size;
    }


    public DragonHead() {

    }


    public void setSize(Double size) {
        this.size = size;
    }


    public Double getSize() {
        return size;
    }


    @Override
    public String toString() {
        try {
            return getSize().toString();
        } catch (NullPointerException e) {
            return "нет инициализации";
        }
    }
}