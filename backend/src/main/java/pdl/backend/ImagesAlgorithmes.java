package pdl.backend;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import boofcv.core.image.ConvertImage;
import boofcv.struct.border.BorderType;

public class ImagesAlgorithmes {

    //THRESHOLD V
    public static void threshold(Planar<GrayU8> image, int t) {
		for (int y = 0; y < image.height; ++y) {
			for (int x = 0; x < image.width; ++x) {
                for (int i = 0; i < image.getNumBands(); i++){
                    int gl = image.getBand(i).get(x, y);
                    if (gl < t) {
                        gl = 0;
                    } else {
                        gl = 255;
                    }
                    image.getBand(i).set(x, y, gl);
                }
			}
		}
	}

    // Mean Filter
    public static void MeanFilterColor(Planar<GrayU8> input, Planar<GrayU8> output, int size) {

        for (int y = 0; y < input.height; ++y) {
          for (int x = 0; x < input.width; ++x) {
              for (int band = 0; band < input.getNumBands(); band++){
                  int gl = 0;
                  int pixel = 0;
                  for (int i = y-size; i <= y +size; i++) {
                      for (int j = x-size; j <= x +size; j++) {
                          if (i>=0 && i<input.height && j>=0 && j<input.width){
                            pixel++;
                            gl += input.getBand(band).get(j,i);
                        }
                      }
                  }
              output.getBand(band).set(x, y, gl/pixel);
              }
          }
      }
    }
   
    // Mean Filter GrayU8
    public static void meanFilterSimple(GrayU8 input, GrayU8 output, int size) {
        if(size%2 == 0) {size= size - 1;}
        int taille = (int)Math.floor(size/2);
        int div = size*size;
        for(int y=taille;y<input.height-taille;y++){
            for (int x=taille;x<input.width-taille;x++) {
                int r = 0;
                int somme = 0;
                for(int v = -taille; v <= taille; v++){
                    for (int u = -taille; u <= taille; u++) {
                        r = r + input.get(x+u,y+v);
                    }        
                }
                somme = r/div;
                output.set(x, y, somme);
            }
        }
      }
    
      public static void meanFilterWithBorders(GrayU8 input, GrayU8 output, int size, BorderType borderType) {
        if (borderType == BorderType.SKIP)
          meanFilterSimple(input, output, size);
        if(borderType == BorderType.NORMALIZED){
    
        }
        else if(borderType == BorderType.EXTENDED || borderType == BorderType.REFLECT){
          int valU = 0;
          int valV = 0;
          if(size%2 == 0) {size= size - 1;}
          int taille = (int)Math.floor(size/2);
          int div = size*size;
          for(int y=taille;y<input.height-taille;y++){
              for (int x=taille;x<input.width-taille;x++) {
                  int r = 0;
                  int somme = 0;
                  for(int v = -taille; v <= taille; v++){
                      for (int u = -taille; u <= taille; u++) {
                        valU = u;
                        valV = v;
                        if (valU < 0 ){
                          if(borderType == BorderType.EXTENDED)
                            valU = 0;
                          if(borderType == BorderType.REFLECT){
                            valU = Math.abs(valU);
                          }
                        }
                            
                        if (valV < 0){
                          if(borderType == BorderType.EXTENDED){
                            //System.out.println("Coucou ! ");
                            valV = 0;
                          }
                          if(borderType == BorderType.REFLECT)
                            valV = Math.abs(valV);
    
                        }
                        r = r + input.get(valU+u,valV+v);
                      }        
                  }
                  somme = r/div;
                  output.set(x, y, somme);
              }
          }
        }
      }

    //LUMI && LUMIRGB V
    public static void lumi(Planar<GrayU8> input, int delta){
        int min = 0;
        int max = 255;
    for (int y = 0; y < input.height; ++y) {
        for (int x = 0; x < input.width; ++x) {
            for (int i = 0; i < input.getNumBands(); i++){
                int gl = input.getBand(i).get(x, y);
                if (gl+delta > min && gl+delta < max)
                    input.getBand(i).set(x, y, gl+delta);
                else{
                    gl = 255;
                    input.getBand(i).set(x, y, gl);
                }
            }
        }
    }
}   
    public static void lumirgb(Planar<GrayU8> input, Planar<GrayU8> output, int delta){
		int min = 0;
		int max = 255;
	for (int y = 0; y < input.height; ++y) {
		for (int x = 0; x < input.width; ++x) {
			for (int i = 0; i < input.getNumBands(); i++){
				if(i<3) {
					int gl = input.getBand(i).get(x, y);
				if (gl+delta > min && gl+delta < max)
					output.getBand(i).set(x, y, gl+delta);
				else{
					gl = 255;
					output.getBand(i).set(x, y, gl);
				}
				}
				else {
					output.getBand(i).set(x, y, input.getBand(i).get(x, y));
				}
				
			}
		}
	}
}
    /// CONVOLUTION V
    public static void convolutionColor(Planar<GrayU8> input, Planar <GrayU8> output, int kernel[][]){
		int size = kernel.length;
		int taille = (int)Math.floor(size/2);
		for(int y=taille;y<input.height-taille;y++){
			for (int x=taille;x<input.width-taille;x++) {
				for (int i = 0; i < input.getNumBands(); i++){
					int r = 0;
					int somme = 0;
					int div = 0;
					for(int v = -taille; v <= taille; v++){
						for (int u = -taille; u <= taille; u++) {
							r = r + input.getBand(i).get(x+u,y+v)*kernel[u+taille][v+taille];
							div += kernel[u+taille][v+taille];
						}        
					}
					somme = r/div;
					output.getBand(i).set(x, y, somme);
				}
			}
		}
	}

    public static void convolution(GrayU8 input, GrayU8 output, int[][] kernel) {
        int size = kernel.length;
        int taille = (int)Math.floor(size/2);
        for(int y=taille;y<input.height-taille;y++){
            for (int x=taille;x<input.width-taille;x++) {
                int r = 0;
                int somme = 0;
                int div = 0;
                for(int v = -taille; v <= taille; v++){
                    for (int u = -taille; u <= taille; u++) {
                        r = r + input.get(x+u,y+v)*kernel[u+taille][v+taille];
                        div += kernel[u+taille][v+taille];
                    }        
                }
                somme = r/div;
                output.set(x, y, somme);
            }
        }
      }

    public static GrayU8 convertToGray(Planar<GrayU8> input) {
		var gray = new GrayU8(input.width, input.height);
		for (int y = 0; y < input.height;y++) {
			for(int x = 0; x < input.width; x++){
				int r = 0;
				double red = (input.getBand(0).get(x,y))*0.3;
				double green = (input.getBand(1).get(x,y))*0.59;
				double blue = (input.getBand(2).get(x,y))*0.11;
				r = (int)(red+green+blue);
				gray.set(x, y, r);
			}
		}
		return gray;
	}

    public static void convertToGray2(Planar<GrayU8> input, Planar<GrayU8> output) {
        for (int y = 0; y < input.height;y++) {
            for(int x = 0; x < input.width; x++){
                int r = 0;
                double red = (input.getBand(0).get(x,y))*0.3;
                double green = (input.getBand(1).get(x,y))*0.59;
                double blue = (input.getBand(2).get(x,y))*0.11;
                r = (int)(red+green+blue);
                for (int c = 0; c < output.getNumBands(); c++) {
                    output.getBand(c).set(x, y, r);
                }
            }
        }
    }
    
    static void rgbToHsv(int r, int g, int b, float[] hsv){
		List<Integer> list = new LinkedList<Integer>();
		list.add(r);
		list.add(g);
		list.add(b);
		float max = Collections.max(list);
		float min = Collections.min(list);

		float deg1 = (float)Math.toRadians(60.0);

		if(max == min){
			hsv[0] = 0;
		}
		else{
			if (max == r){
				float deg2 = (float)Math.toRadians(360.0);
				float div = (g-b)/(max-min);
				hsv[0] = (deg1*div+deg2)%deg2;
			}
			if (max == g){
				float deg2 = (float)Math.toRadians(120.0);
				float div = (b-r)/(max-min);
				hsv[0] = (deg1*div+deg2);
			}
			if (max == b){
				float deg2 = (float)Math.toRadians(240.0);
				float div = (r-g)/(max-min);
				hsv[0] = (deg1*div+deg2);
			}
		}

		if(max == 0){
			hsv[1] = 0;
		}
		else{
			hsv[1] = 1 - (min/max);
		}

		hsv[2] = max;
	}

    static void hsvToRgb(float h, float s, float v, int[] rgb){
		float t = (h/60)%6;
		float f = (h/60)-t;
		float l = v*(1-s);
		float m = v*(1-f*s);
		float n = v*(1-(1-f)*s);

		if(t == 0){
			rgb[0] = (int) v;
			rgb[1] = (int) n;
			rgb[2] = (int) l;
		}
		if(t == 1){
			rgb[0] = (int) m;
			rgb[1] = (int) v;
			rgb[2] = (int) l;
		}
		if(t == 2){
			rgb[0] = (int) l;
			rgb[1] = (int) v;
			rgb[2] = (int) n;
		}
		if(t == 3){
			rgb[0] = (int) l;
			rgb[1] = (int) m;
			rgb[2] = (int) v;
		}
		if(t == 4){
			rgb[0] = (int) n;
			rgb[1] = (int) l;
			rgb[2] = (int) v;
		}
		if(t == 5){
			rgb[0] = (int) v;
			rgb[1] = (int) l;
			rgb[2] = (int) m;
		}
	}
    static void tintedImage(Planar<GrayU8> input, Planar<GrayU8> output, float teinte){
		float[] hsv = new float[3];
		int[] rgb = new int[3];
		for (int y = 0; y < input.height;y++) {
			for(int x = 0; x < input.width; x++){
				int red = (input.getBand(0).get(x,y));
				int green = (input.getBand(1).get(x,y));
				int blue = (input.getBand(2).get(x,y));
				rgbToHsv(red, green, blue, hsv);
				hsvToRgb(teinte,hsv[1],hsv[2],rgb);
				output.getBand(0).set(x, y,rgb[0]);
				output.getBand(1).set(x, y,rgb[1]);
				output.getBand(2).set(x, y,rgb[2]);
			}
		}
	}
    public static int kernelFactor (int [][]tab){
        int cpt = 0;
        for (int[] ints : tab) {
            for (int j = 0; j < tab[0].length; j++) {
                cpt += Math.abs(ints[j]);
            }
        }
        return cpt;
    }


    // HISTROGRAMM EQUALIZATION NB
    public static int[]  calculHistoLut(GrayU8 input) {
        int nbPix=input.height*input.width;
        int[] histogram = new int[256];
        for (int y = 0 ; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                histogram[input.get(x,y)] = histogram[input.get(x,y)] += 1;
            }
        }
        int[] cumulativeHistogram= new int[256];
        cumulativeHistogram[0] = histogram[0];
        for (int i = 1 ; i < cumulativeHistogram.length;i++){
            cumulativeHistogram[i] = histogram[i] +cumulativeHistogram[i-1];
        }
        int[] lut= new int[256];
        for (int i = 1 ; i < lut.length;i++){
            lut[i] = (cumulativeHistogram[i]*255)/nbPix;
        }
        return lut;
    }
    // HISTROGRAMM EQUALIZATION RGB
   

    
    public static void historgb(Planar<GrayU8> input, Planar<GrayU8> output) {
        Planar<GrayU8> grayVersion = input.createSameShape();
        convertToGray2(input, grayVersion);
        int[] lut = calculHistoLut(grayVersion.getBand(0));
        for (int i = 0; i < input.getNumBands(); i++) {
            GrayU8 channelIn = input.getBand(i);
            GrayU8 channelOut = output.getBand(i);
            for (int y = 0; y < input.height; y++) {
                for (int x = 0; x < input.width; x++) {
                    channelOut.set(x, y, lut[channelIn.get(x, y)]);
                }
            }
        }
    }
    
    

    //RGB TO HSV V
    static int min(int r,int g,int b){
        int min=r;
        if (g<min){
         min=g;
        }
        if (b<min){
            min=b;
        }
        return min;
    }
    static int max(int r,int g,int b){
        int max=r;
        if (g>max){
            max=g;
        }
        if (b>max){
            max=b;
        }
        return max;
    }


    // COLORING FILTER V
    public static void teinteImage(Planar<GrayU8>input,Planar<GrayU8>output,float delta){
        for(int y=0;y<input.height;y++){
            for(int x=0;x<input.width;x++){
                int[] rgb=new int[3];
                for(int i=0;i<input.getNumBands();i++){
                    rgb[i]=input.getBand(i).get(x,y); 
                } 
                float[] hsv=new float[3];
                rgbToHsv(rgb[0] , rgb[1], rgb[2], hsv);
                hsvToRgb(delta , hsv[1], hsv[2], rgb);
                for(int i=0;i<input.getNumBands();i++){
                    output.getBand(i).set(x,y,rgb[i]); 
                } 
            } 
        }      
    } 

    // SOBEL V
    

    public static void gradientImageSobel(GrayU8 input, GrayU8 output) {

        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };
    
        int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };
        for (int y = (sobelY.length - 1) / 2; y <= (input.getHeight() - ((sobelY.length + 1) / 2)); ++y){
            for (int x = ((sobelX[0].length - 1) / 2) ; x <= input.getWidth()-((sobelX[0].length+1)/2);++x){
                int valx = 0;
                int valy = 0;
                for (int u = -((sobelX.length - 1)/2); u <= ((sobelX.length - 1) / 2); ++u){
                    for (int v = -((sobelX[0].length - 1)/2); v <= ((sobelX[0].length - 1) / 2); ++v){
                        valx += sobelX[u+((sobelX.length - 1)/2)][v+((sobelX.length - 1)/2)] * input.get(x + u, y + v);
                        valy += sobelY[u+((sobelY.length - 1)/2)][v+((sobelY.length - 1)/2)] * input.get(x + u, y + v) ;
                    }
                }
                double magnitude = Math.hypot(valx, valy);
                if (magnitude > 255) {
                    magnitude = 255;
                }
                output.set(x, y, (int) magnitude);
            }
        }
    }
    
    //SOBEL RGB V
    public static void gradientImageSobelRGB(Planar<GrayU8> input, Planar<GrayU8> output) {
        Planar<GrayU8> gray=input.clone();
        convertToGray2(input, gray);
        for(int i=0;i<input.getNumBands();i++){
            gradientImageSobel(gray.getBand(i),output.getBand(i));
        }
    }

    //GAUSS NB
    public static void GaussFilter(GrayU8 input, GrayU8 output){
        int [][] GaussKernel = {
                {1,2,3,2,1},
                {2,6,8,6,2},
                {3,8,10,8,3},
                {2,6,8,6,2},
                {1,2,3,2,1},};
                convolution(input,output,GaussKernel);
    }


    //GAUSS RGB
    public static void GaussFilterRGB(Planar<GrayU8> image, Planar<GrayU8>output){
        for (int i=0;i< image.getNumBands();i++){
            GaussFilter(image.getBand(i),output.getBand(i));
        }
    }

}
