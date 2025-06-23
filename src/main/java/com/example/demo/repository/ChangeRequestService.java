package com.example.demo.repository;

import com.example.demo.model.BusinessException;
import com.example.demo.model.ChangeRequestModel;

/**
 * Service interface for ChangeRequestModel.
 */
public interface ChangeRequestService {

    /**
     * Find by id change request model.
     *
     * @param id the id
     * @return the change request model
     * @throws BusinessException the business exception
     */
    ChangeRequestModel findById(Long id) throws BusinessException;


}
