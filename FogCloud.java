import java.util.*;

public class FogCloud{
	//EUCLIDEAN DISTANCE
	double euc_distance(int x1,int y1,int x2,int y2){
		
		return Math.sqrt(
            (x2 - x1) *  (x2 - x1) +  (y2-y1) *  (y2-y1)
        );

	}

	public static void main(String args[]){
		
		//DECLARATION
		int x,y;
		FogCloud ob=new FogCloud();             // main class object
		CityPoint[] lc=new CityPoint[100];      // City co-ordinates
		int[] ntnc=new int[100];                // Number of terminal nodes in each cluster
		double[] data_gen=new double[100];
		DCPoint[] ld=new DCPoint[100];          // Data Center co-ordinates 
		FogInstance[] fi=new FogInstance[100];  // fog instances and their values
		Random rn=new Random();                 // Random object is created
		
		//INITIALIZATION
		int min=700;
		int max=900;
		int termi_num=0;
		for(int i=0;i<100;i++){                  //Assinging number of terminal nodes in each cluster 
			ntnc[i]= min + rn.nextInt(max- min + 1);
			termi_num+=ntnc[i];
		}
		
		for(int i=0;i<100;i++){					//Inserting city points using random function
			x=rn.nextInt(350);
			y=rn.nextInt(140);
			lc[i] =  new CityPoint(x,y,0);
		}
		
		for(int i=0;i<8;i++){					//Inserting data center points using random function
			int index= rn.nextInt(4);
			x=rn.nextInt(350);
			y=rn.nextInt(140);
			ld[i] =  new DCPoint(x,y,i+1,index);
		}
		
		//VERIFICATION					//printing both city and data center points 
		System.out.println("\n");
		System.out.println("******************** CITY CO-ORDINATES ********************");
		System.out.println("\t\t                  x\t\ty            ");
		System.out.println("------------------------------------------------------------");
		for(int i=0;i<100;i++){
			System.out.println("Co-ordinates of city "+(i+1)+" are\t "+lc[i].x+"\t\t"+lc[i].y);
		}
		
		System.out.println("\n");		
		System.out.println("******************** DATA CENTER CO-ORDINATES *********************");
		System.out.println("\t\t                 \t x\t\ty            ");
		System.out.println("--------------------------------------------------------------------");
		for(int i=0;i<8;i++){
			System.out.println("Co-ordinates of Data Center "+(i+1)+" are\t"+ld[i].x+"\t\t"+ld[i].y);
		}
		
		//CALCULATION					//Assigning 100 Cities to 8 data centers from values 1 to 8 using Eucledian distance(Clustering)
		System.out.println("\n");		
		System.out.println("*************** CITIES and CORRESPONDING DCs *****************");
		System.out.println("\t\t\t\t    DC Value\t    Distance            ");
		System.out.println("---------------------------------------------------------------");
		int x1,x2,y1,y2,flag;
		double dist,min_dis;
		for(int i=0;i<100;i++){
			x1=lc[i].x;
			y1=lc[i].y;
			min_dis= Integer.MAX_VALUE;
			for(int j=0;j<8;j++){
				x2=ld[j].x;
				y2=ld[j].y;
				dist=ob.euc_distance(x1,y1,x2,y2);
				
				if(dist< min_dis){
					min_dis=dist;
					lc[i].d_val=j+1;
				}
				
			}
			System.out.println("DC Assigned to City "+(i+1)+"  is   \t:\t"+ lc[i].d_val+"\t\t"+Math.round(min_dis));
		}
		System.out.println("\n");		
		System.out.println("Total Number of terminal nodes in all Virtual Clusters\t:\t"+ termi_num);
		
		
		//DATA GENERATION In Giga Bytes
		double data_sum=0;
		for(int i=0;i<100;i++){
			min=34;
			max=65550;
			int pkt_size=min+rn.nextInt(max-min+1);
			data_gen[i]=ntnc[i] * pkt_size;
			data_sum+=data_gen[i];
			
			data_gen[i]=data_gen[i]/Math.pow(10,9);
			
			
		}
		data_sum=data_sum/Math.pow(10,9);
		System.out.println("Total amount of data generated from all Terminal Nodes\t:\t"+(Math.round(data_sum*100)/100.00)+" GB");
		
		
		
		//========================POWER CONSUMPTION==================================================
		
		System.out.println("\n");		
		System.out.println("***************************** POWER CONSUMPTION *******************************");
		System.out.println("\t\t\t\t\t\t\t    Power[Kilo Watts]");
		System.out.println("--------------------------------------------------------------------------------");
		double edge_power,swi_power,storage_power,computing_power,cloud_power,c_sum=0,pow_sum=0,loop_pow;
		double cloud_sum=0,tmp=0,cloud_data=0;
		double[] theta={0.05,0.25,0.5,0.75,1};
		
		for(int i=0;i<100;i++){                     // Power consumed by edge gateways
			edge_power= 20 * data_gen[i];
			pow_sum+=edge_power;
		}
		
		for(int i=0; i<100;i++){				    // Power consumed by switches in Fog Instance
			min=10;
			max=20;
			int swi_num=min+rn.nextInt(max-min+1);	
			swi_power=350 * swi_num; 
			pow_sum+=swi_power;
		}
						 
		storage_power= 20 * 100;					// Power consumed by Storage in 100 Fog Instances
		pow_sum+=storage_power;
		
		computing_power= 3.7 * 100;					// Power consumed by Computing devices in 100 Fog Instances
		pow_sum+=computing_power;
					
		for(int i=0;i<8;i++){						// Power consumed by Data Centers
			pow_sum+=ld[i].power;
		}
		
		for(int i=0;i<100;i++){                     // Power consumed by CLOUD gateways
			cloud_power= 40 * data_gen[i] * Math.pow(10,9);
			cloud_sum+=cloud_power;
		}
		loop_pow=pow_sum;
		cloud_sum/=Math.pow(10,9);
		for(int i=0;i<5;i++){
			pow_sum=loop_pow;
			cloud_data=cloud_sum;
			cloud_data*=theta[i];
			pow_sum+=cloud_data;
			tmp=pow_sum/Math.pow(10,3);
			System.out.println("Total Power Consumption of "+Math.round(100 * theta[i])+" % of total FOG data is\t:\t"+(Math.round(tmp*100)/100.00));
		}
		
		// CONVENTIONAL CLOUD COMPUTING
		
		for(int i=0;i<8;i++){						// Power consumed by Cloud Data Centers
			c_sum+=ld[i].power;
		}
		
		for(int i=0;i<100;i++){                     // Power consumed by CLOUD gateways
			cloud_power= 40 * data_gen[i] * Math.pow(10,9);
			c_sum+=cloud_power;
		}
		c_sum/=Math.pow(10,7);
		System.out.println("Total Power Consumption of Cloud Computing  is\t\t :\t"+(Math.round(c_sum*100)/100.00));

		
		//============COST EVALUATION ==================================================================
		
		System.out.println("\n");		
		System.out.println("***************************** COST EVALUATION ********************************");
		System.out.println("\t\t\t\t\t\t\t    Total Cost[in $]");
		System.out.println("-------------------------------------------------------------------------------");
		
		int cost_sum=0,loop_cost,co_sum=0;
		double tmp_min,tmp_max,upload_cost,upload_data; 
		
		cost_sum+=200 * 50/(365*24*60*60);              //cost 1 Gbps(100) and 10(100) Gbps routers
		
		min=30;											// cost for Electricity consumed in Mega Watts  
		max=70;
		cost_sum+=(min+rn.nextInt(max-min+1)) * tmp/Math.pow(10,3);
		
		for(int i=0;i<8;i++){				               //Cost of servers in Data Centers
			cost_sum+=ld[i].capacity * 4000/(365*24*60*60);
		}
		upload_cost=12 * data_sum;     //cost for Uploading data to Servers  
		loop_cost=cost_sum;
		for(int i=0;i<5;i++){
			cost_sum=loop_cost;
			upload_data=upload_cost;
			upload_data*=theta[i];
			cost_sum+=upload_data;
			
			/*tmp_min=0.45; 									//cost for Storing data in Servers
			tmp_max=0.55;*/
			cost_sum+=/*(tmp_min+rn.nextInt(tmp_max-tmp_min+1))*/ 0.50 * data_sum * theta[i];
			
			System.out.println("Total cost for uploading "+Math.round(100 * theta[i])+" % of total FOG data is\t:\t"+cost_sum * (365*24));
		}
		
		// CONVENTIONAL CLOUD COMPUTING
		co_sum+=100 * 50/(365*24*60*60);                //cost 1 Gbps(100) and 10(100) Gbps routers
		min=30;											// cost for Electricity consumed in Mega Watts  
		max=70;
		co_sum+=(min+rn.nextInt(max-min+1)) * tmp/Math.pow(10,3);
		for(int i=0;i<8;i++){				               //Cost of servers in Data Centers
			co_sum+=ld[i].capacity * 4000/(365*24*60*60);
		}
		upload_cost=12 * data_sum;     //cost for Uploading data to Servers  
		co_sum+=upload_cost;
		co_sum+=data_sum;
		System.out.println("Total cost for Cloud Computing is\t\t\t:\t"+co_sum * (365*24*60));
		

	}
}




class CityPoint{					      //CityPoint class for assinging x,y co-ordinates
	int x,y,d_val;
	
	CityPoint(int x,int y, int d_val){    //Parameterised Constructor
		this.x=x;
		this.y=y;
		this.d_val=d_val;

	}
	CityPoint(){}						  // Default Constructor
	
}


class DCPoint extends CityPoint{   //DCPoint class extends CityPoint class for assinging x,y co-ordinates
	int index;
	double capacity,power,upload_cost;
	double[] ser_cap={16000,32000,64000,128000};
	double[] ser_pow={9.7,19.4,38.7,77.4};
	DCPoint(int x,int y, int d_val,int index){
		super(x,y,d_val);
		this.index=index;
		capacity=ser_cap[index];            //number of servers in each data center
		power=(ser_pow[index] * capacity)/Math.pow(10,6);    //power consumed by each data center
	}
	
}


class FogInstance{				//FogInstance class is for assinging different values in Fog tier
	int router_cost;
	int router_capacity;
	int router_pow;
	public static final int switch_pow= 350;
	public static final int storage_pow=600;
	public static final double computing_pow=3.7;
}

