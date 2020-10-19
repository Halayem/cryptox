package fr.enix.exchanges.mapper;

import fr.enix.exchanges.model.repository.PriceReference;

public class PriceReferenceMapper {

    public fr.enix.exchanges.model.dto.PriceReference mapFromRepositoryToDto(final PriceReference priceReference) {
        return fr.enix.exchanges.model.dto.PriceReference
                .builder()
                .applicationAssetPair   (priceReference.getApplicationAssetPair()   )
                .price                  (priceReference.getPrice()                  )
                .updatedBy              (priceReference.getUpdatedBy()              )
                .updatedAt              (priceReference.getDatetime()               )
                .build();
    }
}
