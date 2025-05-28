package com.snipe.learning.AOP;

import java.util.Locale;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.snipe.learning.exception.UPLException;
import com.snipe.learning.model.PageResponse;
import com.snipe.learning.utility.ApiResponse;
import com.snipe.learning.utility.LoggerUtil;

@Component
public class HandlerService {
	
	 @Autowired
	    private MessageSource messageSource;

	 @Autowired
	    private LoggerUtil loggerUtil;


public ResponseEntity<?> handleServiceCall(Supplier<Object> action, String successMessageKey) {
    try {
        Object result = action.get();

        String msg = messageSource.getMessage(successMessageKey, null, Locale.getDefault());
        loggerUtil.logInfo(msg);

        if (result instanceof Page<?>) {
            Page<?> pageResult = (Page<?>) result;
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    msg,
                    pageResult.getContent(),
                    (int) pageResult.getTotalElements()
            ));
        } else if (result instanceof PageResponse<?>) {
            PageResponse<?> pageResponse = (PageResponse<?>) result;
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    msg,
                    pageResponse.getContent(),
                    (int) pageResponse.getTotalElements()
            ));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    msg,
                    result,
                    0
            ));
        }
    } catch (UPLException e) {
        loggerUtil.logError(e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse<>(false, e.getMessage(), null, 0)
        );
    } catch (Exception e) {
        String err = messageSource.getMessage("ERROR.COMMON_MSG", null, Locale.getDefault());
        loggerUtil.logError(err, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse<>(false, err, null, 0)
        );
    }
}
}