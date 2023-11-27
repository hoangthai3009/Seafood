package ChuyenNganh.Seafood.Mapper;

import ChuyenNganh.Seafood.DTO.BillDto;
import ChuyenNganh.Seafood.Entity.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapperImpl implements BillMapper {
    @Override
    public BillDto toDto(Bill bill) {
        BillDto dto = new BillDto();
        dto.setBillId(bill.getId());
        dto.setTotalPrice(bill.getTotalPrice());
        dto.setCreatedAt(bill.getCreatedAt());
        dto.setName(bill.getUser().getFullname());
        return dto;
    }
}
