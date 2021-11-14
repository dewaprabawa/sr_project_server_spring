package learn_rest.example.tell_me_this_will_work.helper;

import java.util.Objects;

public class FinalResult<T> {
    boolean success;
    public String statusCode;
    public String message;
    T data;

    public FinalResult(boolean success, String statusCode, String message, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalResult<?> that = (FinalResult<?>) o;
        return success == that.success && Objects.equals(statusCode, that.statusCode) && Objects.equals(message, that.message) && Objects.equals(data, that.data);
    }

    public boolean getSuccess(boolean success) {
        return success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, statusCode, message, data);
    }

    public String getMessage() {
        return message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public T getData() {
        return data;
    }


}
