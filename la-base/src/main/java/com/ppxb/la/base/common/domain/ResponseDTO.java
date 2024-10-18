package com.ppxb.la.base.common.domain;

import com.ppxb.la.base.common.code.ErrorCode;
import com.ppxb.la.base.common.code.UserErrorCode;
import com.ppxb.la.base.common.enumeration.DataTypeEnum;
import com.ppxb.la.base.common.swagger.SchemaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Schema
public class ResponseDTO<T> {

    public static final int OK_CODE = 0;

    public static final String OK_MSG = "操作成功";

    @Schema(description = "返回码")
    private Integer code;

    @Schema(description = "级别")
    private String level;

    private String msg;

    private Boolean ok;

    @Schema(description = "返回数据")
    private T data;

    @SchemaEnum(value = DataTypeEnum.class, desc = "数据类型")
    private Integer dataType;

    public ResponseDTO(Integer code, String level, boolean ok, String msg, T data) {
        this.code = code;
        this.level = level;
        this.msg = msg;
        this.ok = ok;
        this.data = data;
        this.dataType = DataTypeEnum.NORMAL.getValue();
    }

    public ResponseDTO(Integer code, String level, boolean ok, String msg) {
        this.code = code;
        this.level = level;
        this.msg = msg;
        this.ok = ok;
        this.dataType = DataTypeEnum.NORMAL.getValue();
    }

    public ResponseDTO(ErrorCode errorCode, boolean ok, String msg, T data) {
        this.code = errorCode.getCode();
        this.level = errorCode.getLevel();
        this.ok = ok;
        if (StringUtils.isNotBlank(msg)) {
            this.msg = msg;
        } else {
            this.msg = errorCode.getMsg();
        }
        this.data = data;
        this.dataType = DataTypeEnum.NORMAL.getValue();
    }

    public static <T> ResponseDTO<T> ok() {
        return new ResponseDTO<>(OK_CODE, null, true, OK_MSG, null);
    }

    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(OK_CODE, null, true, OK_MSG, data);
    }

    public static <T> ResponseDTO<T> okMsg(String msg) {
        return new ResponseDTO<>(OK_CODE, null, true, msg, null);
    }

    public static <T> ResponseDTO<T> userErrorParam() {
        return new ResponseDTO<>(UserErrorCode.PARAM_ERROR, false, null, null);
    }

    public static <T> ResponseDTO<T> userErrorParam(String msg) {
        return new ResponseDTO<>(UserErrorCode.PARAM_ERROR, false, msg, null);
    }

    public static <T> ResponseDTO<T> error(ErrorCode errorCode) {
        return new ResponseDTO<>(errorCode, false, null, null);
    }

    public static <T> ResponseDTO<T> error(ErrorCode errorCode, boolean ok) {
        return new ResponseDTO<>(errorCode, ok, null, null);
    }

    public static <T> ResponseDTO<T> error(ResponseDTO<?> responseDTO) {
        return new ResponseDTO<>(responseDTO.getCode(), responseDTO.getLevel(), responseDTO.getOk(), responseDTO.getMsg(), null);
    }

    public static <T> ResponseDTO<T> error(ErrorCode errorCode, String msg) {
        return new ResponseDTO<>(errorCode, false, msg, null);
    }

    public static <T> ResponseDTO<T> errorData(ErrorCode errorCode, T data) {
        return new ResponseDTO<>(errorCode, false, null, data);
    }
}
