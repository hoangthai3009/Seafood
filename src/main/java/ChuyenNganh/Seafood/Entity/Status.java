package ChuyenNganh.Seafood.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    ONLINE, OFFLINE
}

