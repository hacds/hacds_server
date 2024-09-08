package diamond.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pager<JSONObject> {
    private int page;
    private int size;
    private List<JSONObject> data;
    private long total;

    @Override
    public String toString() {
        return "Pager{" +
                "page=" + page +
                ", size=" + size +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}


