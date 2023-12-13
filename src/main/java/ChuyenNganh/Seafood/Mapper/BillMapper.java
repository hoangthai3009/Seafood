package ChuyenNganh.Seafood.Mapper;

import ChuyenNganh.Seafood.DTO.BillDto;
import ChuyenNganh.Seafood.Entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    @Mapping(source = "user.fullname", target = "name")
    BillDto toDto(Bill bill);

}
