package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import Model.Vehicle;

import java.util.Map.Entry;

@Service
public class VehicleService {
	TreeMap<BigDecimal,ArrayList<Vehicle>> vehicles;
	TreeMap<String,ArrayList<BigDecimal>> type ;
	TreeMap<String,ArrayList<BigDecimal>> brand;
	TreeMap<String,ArrayList<BigDecimal>> engine;
	TreeMap<String,ArrayList<BigDecimal>> color;
	Vehicle v;
	public VehicleService(){
		vehicles = new TreeMap<BigDecimal,ArrayList<Vehicle>>();
		type = new TreeMap<String,ArrayList<BigDecimal>>();
		brand = new TreeMap<String,ArrayList<BigDecimal>>();
		engine = new TreeMap<String,ArrayList<BigDecimal>>();
		color = new TreeMap<String,ArrayList<BigDecimal>>();		
		v = new Vehicle();
	}
	//Case 1: Returns list of vehicles sorted by price
	public TreeMap<BigDecimal,ArrayList<Vehicle>> getAll() throws IOException{
		   parseFile();
		   return vehicles;
	}
    
	//File parser to parse the text file and set each vehicle field
	private void parseFile() throws FileNotFoundException, IOException {
		  BufferedReader bf = new BufferedReader(new FileReader("/Users/Anjali/Documents/workspace3/CarDealership/src/main/java/service/vehicles"));
		  String fileRead = bf.readLine();
		  while(fileRead != null){
			   populateFields(fileRead);
			   fileRead = bf.readLine();			  
		  }	  
           bf.close();
	}
    //Sets vehicle fields
	private void populateFields(String fileRead) {
		String [] split = fileRead.split(",");
		  v.setType(split[0].trim());
		  v.setVIN(split[1].trim());
		  v.setBrand(split[2].trim());
		  v.setColor(split[3].trim());
		  v.setEngine(split[4].trim());
		  v.setPrice(new BigDecimal(split[5].trim()));
		  v.setYear(Integer.parseInt(split[6].trim()));
		  
		  if(!vehicles.containsKey(v.getPrice())){
			  vehicles.put(v.getPrice(), new ArrayList<Vehicle>());
		  }
		   vehicles.get(v.getPrice()).add(v);
		   v = new Vehicle();
	}

	//Returns average cost per color of the vehicle sorted by price
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostColor(){
		for(Entry<BigDecimal, ArrayList<Vehicle>> entry: vehicles.entrySet()){
			ArrayList<Vehicle> al = entry.getValue();
			for(int i=0;i<al.size();i++){
				if(!color.containsKey(al.get(i).getColor())){
					color.put(al.get(i).getColor(), new ArrayList<BigDecimal>());
				}
				color.get(al.get(i).getColor()).add(entry.getKey());
			}
		}
		
		TreeMap<BigDecimal,ArrayList<String>> costPerColor = getCostPerColor(color);
		return costPerColor;
				
	}

	//Returns average cost per engine type of the vehicle sorted by price
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostEngine(){
		for(Entry<BigDecimal, ArrayList<Vehicle>> entry: vehicles.entrySet()){
			ArrayList<Vehicle> al = entry.getValue();
			for(int i=0;i<al.size();i++){
				if(!engine.containsKey(al.get(i).getEngine())){
					engine.put(al.get(i).getEngine(), new ArrayList<BigDecimal>());
				}
				engine.get(al.get(i).getEngine()).add(entry.getKey());
			}
		}
		
		TreeMap<BigDecimal,ArrayList<String>> costPerEngine = getCostPerEngine(engine);
		return costPerEngine;
				
	}

    //Returns average cost per brand of the vehicle sorted by price
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostBrand(){
		for(Entry<BigDecimal, ArrayList<Vehicle>> entry: vehicles.entrySet()){
			ArrayList<Vehicle> al = entry.getValue();
			for(int i=0;i<al.size();i++){
				if(!brand.containsKey(al.get(i).getBrand())){
					brand.put(al.get(i).getBrand(), new ArrayList<BigDecimal>());
				}
				brand.get(al.get(i).getBrand()).add(entry.getKey());
			}
		}
		
		TreeMap<BigDecimal,ArrayList<String>> costPerBrand = getCostPerBrand(brand);
		return costPerBrand;
				
	}
	//Returns average cost per type of the vehicle sorted by price
	public TreeMap<BigDecimal,ArrayList<String>> getAverageCostType(){
		for(Entry<BigDecimal, ArrayList<Vehicle>> entry: vehicles.entrySet()){
			ArrayList<Vehicle> al = entry.getValue();
			for(int i=0;i<al.size();i++){
				if(!type.containsKey(al.get(i).getType())){
					type.put(al.get(i).getType(), new ArrayList<BigDecimal>());
				}
				type.get(al.get(i).getType()).add(entry.getKey());
			}
		}
		
		TreeMap<BigDecimal,ArrayList<String>> costPerType = getCostPerType(type);
		return costPerType;
				
	}
	
	public TreeMap<BigDecimal,ArrayList<String>> getCostPerType(TreeMap<String,ArrayList<BigDecimal>> tm){
		TreeMap<BigDecimal, ArrayList<String>> costPertype = calculateAvg(tm);
		return costPertype;
	}
    
	//Calculates average cost per selected criteria such as type, brand,color, engine type etc.
	private TreeMap<BigDecimal, ArrayList<String>> calculateAvg(
			TreeMap<String, ArrayList<BigDecimal>> tm) {
		TreeMap<BigDecimal,ArrayList<String>> costPertype = new TreeMap<BigDecimal,ArrayList<String>>();
		BigDecimal sum=new BigDecimal(0);
		BigDecimal avg =new BigDecimal(0);
		for(Entry<String, ArrayList<BigDecimal>> entry: tm.entrySet()){
			ArrayList<BigDecimal> al = entry.getValue();
			for(int i=0;i<al.size();i++){
				sum = sum.add(al.get(i));
			}
			avg = sum.divide(new BigDecimal(al.size()), RoundingMode.HALF_UP);
			if(!costPertype.containsKey(avg)){
				costPertype.put(avg, new ArrayList<String>());
			}
			costPertype.get(avg).add(entry.getKey());
			
		}
		return costPertype;
	}
	
	public TreeMap<BigDecimal,ArrayList<String>> getCostPerBrand(TreeMap<String,ArrayList<BigDecimal>> tm){
		TreeMap<BigDecimal, ArrayList<String>> costPerbrand = calculateAvg(tm);
		return costPerbrand;
	}
	
	public TreeMap<BigDecimal,ArrayList<String>> getCostPerEngine(TreeMap<String,ArrayList<BigDecimal>> tm){
		TreeMap<BigDecimal, ArrayList<String>> costPerengine = calculateAvg(tm);
		return costPerengine;
	}

	public TreeMap<BigDecimal,ArrayList<String>> getCostPerColor(TreeMap<String,ArrayList<BigDecimal>> tm){
		TreeMap<BigDecimal, ArrayList<String>> costPerColor = calculateAvg(tm);
		return costPerColor;
	}


}
