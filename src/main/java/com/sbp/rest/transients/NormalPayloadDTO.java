package com.sbp.rest.transients;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NormalPayloadDTO {

    String name ;
    int value ;

}
