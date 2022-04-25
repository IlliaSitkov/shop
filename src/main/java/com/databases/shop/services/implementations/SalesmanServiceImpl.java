package com.databases.shop.services.implementations;

import com.databases.shop.exceptions.salesman.NoSalesmanWithSuchIdException;
import com.databases.shop.exceptions.salesman.SalesmanRegistrationException;
import com.databases.shop.mapstruct.dtos.filterBoundsDtos.SalesmanFilterBoundsDto;
import com.databases.shop.mapstruct.dtos.salesman.SalesmanGetDto;
import com.databases.shop.mapstruct.dtos.salesman.SalesmanPostDto;
import com.databases.shop.mapstruct.mappers.SalesmanMapper;
import com.databases.shop.models.Order;
import com.databases.shop.models.Salesman;
import com.databases.shop.models.Telephone;
import com.databases.shop.repositories.*;
import com.databases.shop.repositories.queryinterfaces.MinMaxValues;
import com.databases.shop.services.interfaces.AdminService;
import com.databases.shop.services.interfaces.SalesmanService;
import com.databases.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SalesmanServiceImpl implements SalesmanService {


    @Autowired
    private SalesmanRepository salesmanRepository;

    @Autowired
    private TelephoneRepository telephoneRepository;

    @Autowired
    private SalesmanFilterRepository salesmanFilterRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private SalesmanMapper salesmanMapper;

    @Autowired
    private AdminService adminService;

    @Override
    public Iterable<Salesman> findAll() {
        return salesmanRepository.getAll();
    }

    @Override
    public Salesman findById(Long id) {
        return salesmanRepository.findById(id).orElseThrow(() -> new NoSalesmanWithSuchIdException(id));
    }

    @Override
    public Salesman save(Salesman salesman) {
        utils.processSalesman(salesman);
        utils.checkPersonName(salesman.getPersonName());
        utils.checkEmail(salesman.getEmail());
        salesman.getTelephones().forEach(t -> utils.checkPhoneNumber(t.getTelNumber()));
        utils.checkDates(salesman.getDateOfBirth(), salesman.getDateOfHiring());
        return salesmanRepository.save(salesman);
    }


    public SalesmanGetDto saveSalesmanPostDto(SalesmanPostDto salesmanPostDto) {
        try {
            Salesman salesmanToSave = salesmanMapper.salesmanSaveDtoToSalesman(salesmanMapper.salesmanPostDtoToSalesmanSaveDto(salesmanPostDto));
            Set<Telephone> telephones = salesmanToSave.getTelephones();
            telephones.forEach(t -> t.setSalesman(salesmanToSave));
            Salesman salesman = save(salesmanToSave);
            adminService.registerUser(salesmanPostDto.getEmail(),salesmanPostDto.getPassword());
            adminService.saveUserToFirestore(salesmanPostDto.getEmail(),salesmanPostDto.getRole());
            return salesmanMapper.salesmanToSalesmanGetDto(salesman);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SalesmanRegistrationException();
        }
    }

    @Override
    public SalesmanFilterBoundsDto getSalesmanFilterBounds() {
        MinMaxValues minMaxOrderCount = salesmanRepository.minMaxOrderCount();
        MinMaxValues minMaxSalesmanIncome = salesmanRepository.minMaxSalesmanIncome();

        SalesmanFilterBoundsDto salesmanFilterBoundsDto = new SalesmanFilterBoundsDto();

        salesmanFilterBoundsDto.setMinOrderCount((int)minMaxOrderCount.getMinValue());
        salesmanFilterBoundsDto.setMaxOrderCount((int)minMaxOrderCount.getMaxValue());

        salesmanFilterBoundsDto.setMinIncome(minMaxSalesmanIncome.getMinValue());
        salesmanFilterBoundsDto.setMaxIncome(minMaxSalesmanIncome.getMaxValue());

        return salesmanFilterBoundsDto;
    }

    @Override
    public Iterable<Salesman> getFilteredSalesmen(double income, int orders, boolean hasAllCategories) {
        return salesmanFilterRepository.filterSalesmen(income,orders,hasAllCategories);
    }

    @Override
    public Salesman findByEmail(String email) {
        return salesmanRepository.getByEmail(email);
    }

    @Override
    public Salesman update(Long id, Salesman salesman) {
        salesman.setPersonName(utils.processPersonName(salesman.getPersonName()));
        utils.checkPersonName(salesman.getPersonName());
        salesman.getTelephones().forEach(t -> utils.checkPhoneNumber(t.getTelNumber()));
        utils.checkDates(salesman.getDateOfBirth(), salesman.getDateOfHiring());
        deleteTelephonesNotInList(salesman.getTelephones());

        Salesman s = salesmanRepository.findById(id).orElseThrow(() -> new NoSalesmanWithSuchIdException(id));
        salesman.getTelephones().forEach(t -> t.setSalesman(s));
        s.setPersonName(salesman.getPersonName());
        s.setTelephones(salesman.getTelephones());
        s.setDateOfBirth(salesman.getDateOfBirth());
        s.setDateOfHiring(salesman.getDateOfHiring());
        return salesmanRepository.save(s);
    }

    private void deleteTelephonesNotInList(Set<Telephone> goodTelephones) {
        Iterable<Telephone> telephones = telephoneRepository.findAll();
        Set<String> ids = goodTelephones.stream().map(Telephone::getTelNumber).collect(Collectors.toSet());
        telephones.forEach(t -> {
            if (!ids.contains(t.getTelNumber())) {
                telephoneRepository.deleteById(t.getTelNumber());
            }
        });
    }

    @Override
    public boolean existsByEmail(String email) {
        return salesmanRepository.existsByEmail(email);
    }


    @Override
    public boolean delete(Long id) {
        if (salesmanRepository.existsById(id)) {
            for (Order order: orderRepository.findBySalesmanId(id)){
                order.setSalesman(null);
                orderRepository.save(order);
            }
            Salesman s = salesmanRepository.getById(id);
            try {
                adminService.deleteUserAccountByEmail(s.getEmail());
                adminService.deleteUserFromFirestore(s.getEmail());
                salesmanRepository.delete(id);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Salesman's account could not have been deleted");
                return false;
            }
        };
        System.out.println("Salesman does not exist");
        return false;

    }


}
