package diamond.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Diamond {


    public Diamond(){
       this.color="";
       this.nameliteral="";
        this.numberliteral="";
       this.styles="";
       this.shapes="";
       this.status="1";
       this.price=0;
       this.groupName="";

    }

    @JSONField(name = "address")
    private String address;

    @JSONField(name = "approx_fee_offer")
    private String approxFeeOffer;

    @JSONField(name = "average_burn_price")
    private String averageBurnPrice;

    @JSONField(name = "block_height")
    private String blockHeight;

    @JSONField(name = "current_block_hash")
    private String currentBlockHash;

    @JSONField(name = "custom_message")
    private String customMessage;

    @JSONField(name = "inscriptions")
    private String inscriptions;
    @JSONField(name = "life_gene")
    private String lifeGene;

    @JSONField(name = "miner_address")
    private String minerAddress;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "nonce")
    private String nonce;

    @JSONField(name = "number")
    private String number;

    @JSONField(name = "prev_block_hash")
    private String prevBlockHash;

    @JSONField(name = "visual_gene")
    private String visualGene;

    @JSONField(name = "shapes")
    private String shapes;

    @JSONField(name = "styles")
    private String styles;

    @JSONField(name = "nameliteral")
    private String nameliteral;

    @JSONField(name = "numberliteral")
    private String numberliteral;


    @JSONField(name = "status")
    private String status;

    @JSONField(name = "price")
    private double price;

    @JSONField(name = "groupName")
    private String groupName;


    @JSONField(name = "color")
    private String color;

    @JSONField(name = "score")
    private int score;

}
