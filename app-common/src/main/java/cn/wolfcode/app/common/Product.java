package cn.wolfcode.app.common;

import lombok.*;

import java.math.BigDecimal;

/**
 * 产品信息
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;//id
    private String sn;//产品编号
    private String name;//产品名称
    private BigDecimal price;//产品价格
}
