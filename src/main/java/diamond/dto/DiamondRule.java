package diamond.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiamondRule {

    private String currency;

    private String type;

    private String typeEn;

    private String metric;

    private String mark;

    private String metric_en;

    private int score;

}
