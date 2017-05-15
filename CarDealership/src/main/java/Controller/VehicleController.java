package Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import service.VehicleService;
import Model.Vehicle;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	
	@Autowired
	VehicleService vs;
	
	@RequestMapping("/all")
	public TreeMap<BigDecimal,ArrayList<Vehicle>> getAll() throws IOException{
		return vs.getAll();
	}
    
	@RequestMapping("/costpertype")
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostType(){
		return vs.getAverageCostType();
	}

	@RequestMapping("/costperbrand")
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostBrand(){
		return vs.getAverageCostBrand();
	}
	
	@RequestMapping("/costperengine")
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostEngine(){
		return vs.getAverageCostEngine();
	}
	
	@RequestMapping("/costpercolor")
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostColor(){
		return vs.getAverageCostColor();
	}
}
