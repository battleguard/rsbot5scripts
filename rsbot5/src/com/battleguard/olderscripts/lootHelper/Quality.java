package LootHelper;

import java.awt.Color;

public enum Quality {
	    GREY    (0x9d9d9d, 0),
	    WHITE   (0xffffff, 100),
	    GREEN   (0xFF03D108, 1000),
	    BLUE    (0x0070dd, 2000),
	    PURPLE  (0xa335ee,   10000),
	    ORANGE  (0xff8000, 50000);	    	    
	    

	    private final int rgb;
	    private int price;
	    
	    Quality(int rgb, int price) {
	        this.rgb = rgb;
	        this.price = price;
	    }
	    
	    public Color color() { 
	    	return new Color(rgb); 
	    }
	    
	    public int price() { 
	    	return price; 
	    }
	    
	    public static final Color getRarity(int lootPrice) {
	    	Quality[] qualities = Quality.values();
	    	for(int i = qualities.length - 1; i >= 0; i--) {
				if(lootPrice >= qualities[i].price) {
					return qualities[i].color();
				}
	    	}
	    	return GREY.color();
	    }
	    
	    public void setPrice(int price) { 
	    	this.price = price;  
	    }
	}
