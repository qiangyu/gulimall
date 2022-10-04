package cn.zqyu.gulimall.product.exception;

import cn.zqyu.common.exception.BizCodeEnume;
import cn.zqyu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 全局异常处理
 *
 * </p>
 *
 * @author zq yu
 * @see cn.zqyu.gulimall.product.exception
 * @see GuliMallExceptionControllerAdvice
 * @since 2022/9/25 21:45
 */
@Slf4j
@RestControllerAdvice
public class GuliMallExceptionControllerAdvice {

    /**
     * <p>
     * 数据检验全局异常处理
     *
     * </p>
     *
     * @param e e
     * @return cn.zqyu.common.utils.R /
     * @author zq yu
     * @since 2022/9/25 21:51
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlerValidException(MethodArgumentNotValidException e) {
        log.error("数据校验异常：{}，异常类型：{}", e.getMessage(), e.getClass());

        Map<String, String> errorMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handlerException(Throwable throwable) {
        log.warn("warn: {}", throwable.toString());
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }

}
