package ChuyenNganh.Seafood.Payload.Request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private String content;
}
