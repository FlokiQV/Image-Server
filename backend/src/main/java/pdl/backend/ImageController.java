package pdl.backend;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


@RestController
public class ImageController {

  @Autowired
  private ObjectMapper mapper;

  private final ImageDao imageDao;

  @Autowired
  public ImageController(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<?> getImage(@PathVariable("id") long id) {

      // Vérifie si l'id est positif
    if (id < 0) {
      return new ResponseEntity<>("id doit être positif", HttpStatus.NOT_FOUND);
    }
    Optional<Image> image = imageDao.retrieve(id);

    if (image.isPresent()) {
      InputStream inputStream = new ByteArrayInputStream(image.get().getData());
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));
    }

      // Si l'image n'est pas trouvée, retourne une réponse HTTP avec un message d'erreur correspondant
    return new ResponseEntity<>("Aucune image existante avec l’identifiant id=" + id, HttpStatus.NOT_FOUND);
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteImage(@PathVariable("id") long id) {

    Optional<Image> image = imageDao.retrieve(id);

        // Si l'image est trouvée, la supprime de la base de données et retourne une réponse HTTP indiquant que l'image a été supprimée avec succès
    if (image.isPresent()) {
      imageDao.delete(image.get());
      return new ResponseEntity<>("Image id=" + id + " deleted.", HttpStatus.OK);
    }
      // Si l'image n'est pas trouvée, retourne une réponse HTTP avec un message d'erreur 
    return new ResponseEntity<>("Image id=" + id + " not found.", HttpStatus.NOT_FOUND);
  }

  
  @RequestMapping(value = "/images", method = RequestMethod.POST)
  public ResponseEntity<?> addImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    
        // Vérifie si le type de fichier est JPEG ou PNG
      String contentType = file.getContentType();
      if (!contentType.endsWith("jpeg")  && !contentType.endsWith("png")) {
        return new ResponseEntity<>("415 Unsupported Media Type ", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

      try {
          imageDao.create(new Image(file.getOriginalFilename(), file.getBytes()));
      } catch (IOException e) {
          return new ResponseEntity<>("Erreur lors de la lecture du fichier", HttpStatus.NO_CONTENT);
      }
    
        // Si l'ajout de l'image est réussi, retourne une réponse HTTP indiquant que l'image a été ajoutée avec succès
      return new ResponseEntity<>("Image téléchargée", HttpStatus.OK);
  }
  
    // Méthode qui retourne l'extension de l'image
  private String getFileType(String fileName) {
      int lastDotIndex = fileName.lastIndexOf('.');
      if (lastDotIndex != -1) {
          return fileName.substring(lastDotIndex + 1).toLowerCase();
      } else {
          return "";
      }
  }
  
  

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public JsonNode getImageList() throws IOException {
  
      /* // Cherche le répertoire "images" dans le backend
    Path baseDirectory = Paths.get("backend","src", "main", "resources");
    Path imagesDirectory = findImagesDirectory(baseDirectory);
    
      // Si le répertoire 'images' n'est pas trouvé, retourne une réponse d'erreur
    if (imagesDirectory == null) {
        String errorMessage = "Le dossier 'images' est introuvable.";
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("error", errorMessage);
        return errorNode;
    } */

    List<Image> images = imageDao.retrieveAll();
    
    // Crée un nœud JSON pour stocker les informations de chaque image
    ArrayNode nodes = mapper.createArrayNode();
    for (Image image : images) {
        ObjectNode objectNode = mapper.createObjectNode();
    
        // Ajout des propriétés de l'image (ID, nom, type, taille)
        objectNode.put("id", image.getId());
        objectNode.put("name", image.getName());
        objectNode.put("type", getFileType(image.getName()));
        
        
        // Calcul des dimensions de l'image à partir de ses données binaires
      try (ByteArrayInputStream bis = new ByteArrayInputStream(image.getData())) {
          BufferedImage bufferedImage = ImageIO.read(bis);
          int width = bufferedImage.getWidth();
          int height = bufferedImage.getHeight();
          objectNode.put("size", " " + width + "*" + height);
      } catch (Exception e) {

      }
        nodes.add(objectNode);
    }
    return nodes;
}

// Méthode récursive qui cherche le répertoire "images" dans un dossier et ses sous-dossiers
private Path findImagesDirectory(Path directory) throws IOException {
  if (Files.exists(directory) && Files.isDirectory(directory)) {

     // Vérifie si le dossier contient un sous-dossier nommé "images"
      Path imagesDirectory = directory.resolve("images");
      if (Files.exists(imagesDirectory) && Files.isDirectory(imagesDirectory)) {

        // Si le dossier "images" est trouvé, retourne son chemin
          return imagesDirectory;
      } else {
          
        // Si le dossier ne contient pas de sous-dossier nommé "images", cherche dans ses sous-dossiers
          try (DirectoryStream<Path> subDirectories = Files.newDirectoryStream(directory)) {
              for (Path subDirectory : subDirectories) {
                  Path imagesSubDirectory = findImagesDirectory(subDirectory);
                  if (imagesSubDirectory != null) {
                          
                    // Si le dossier "images" est trouvé, retourne son chemin
                      return imagesSubDirectory;
                  }
              }
          }
      }
  }
  return null;}


  // Besoin 8 : Exécution d’algorithmes par le serveur

  @RequestMapping (value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE, params = {"algorithm", "p1", "p2"})
  public ResponseEntity<?> getImage(@PathVariable("id") long id, @RequestParam("algorithm") String Algo, @RequestParam("p1") Float p1, @RequestParam("p2") Float p2) {
    Optional<Image> image = imageDao.retrieve(id);
    System.out.println("First Parameter");

    if (!image.isPresent()){
      return new ResponseEntity<>("Image requested id = " + id + "is not found!", HttpStatus.NOT_FOUND);
    }

    Optional<Image> image_2 = Optional.empty();
    float[] option = {p2};

    if (Algo.compareTo("flou") == 0) {
      if (p1 == 0) {        // if first parameter is 0 apply mean filter
        try {
          image_2 = imageDao.processing(EnumProcessing.MeanFilterColor, image.get(), option);
        }
          catch (Exception e){
            return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
          }
      }
      if (p1 == 1) {        // if first parameter is 1 apply Gauss filter
        try {
          image_2 = imageDao.processing(EnumProcessing.FLOUGAUSS, image.get(), option);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
      } else {
        return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
      }

      // Error if the image after applying algo is missing
      if (!image_2.isPresent()){
        return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
      }

    }
    InputStream inputStream = new ByteArrayInputStream(image_2.get().getData());
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));

 }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE, params = {"algorithm", "p1"})
  public ResponseEntity<?> getImage (@PathVariable("id") long id, @RequestParam ("algorithm") String Algo, @RequestParam ("p1") Float p1) {

    Optional<Image> image = imageDao.retrieve(id);
    System.out.println("First Parameter");

    if (!image.isPresent()){
      return new ResponseEntity<>("Image requested id = " + id + "is not found!", HttpStatus.NOT_FOUND);
    }

    Optional<Image> image_2 = Optional.empty();
    float[] option = {p1};

    if (Algo.compareTo("Color") == 0){
      try {
        image_2 = imageDao.processing(EnumProcessing.COLOR, image.get(), option);
      } catch (Exception e){
          return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR); 
      }
    } else if (Algo.compareTo("IncreaseLuminosity") == 0){
      try {
        image_2 = imageDao.processing(EnumProcessing.LUMI, image.get(), option);
      } catch (Exception e){
        return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
    }
    if (!image_2.isPresent()){
      return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    InputStream inputStream = new ByteArrayInputStream(image_2.get().getData());
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));
  }

  
  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE, params = {"algorithm"})
  public ResponseEntity<?> getImage (@PathVariable("id") long id, @RequestParam ("algorithm") String Algo) {
    Optional<Image> image = imageDao.retrieve(id);
    System.out.println("Algorithm name parameter");

    if (!image.isPresent()){
      return new ResponseEntity<>("Image requested id = " + id + "is not found!", HttpStatus.NOT_FOUND);
    }

    Optional<Image> image_2 = Optional.empty();
    float[] option = {};

    if (Algo.compareTo("HistoEqualisation") == 0) {
      try {
        image_2 = imageDao.processing(EnumProcessing.HISTO, image.get(), option);
      } catch (Exception e){
        return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else if (Algo.compareTo("Sobel") == 0){
      try {
        image_2 = imageDao.processing(EnumProcessing.SOBEL, image.get(), option);
      } catch (Exception e){
        return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      if (!image_2.isPresent()){
        return new ResponseEntity<>("Internal Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    InputStream inputStream = new ByteArrayInputStream(image_2.get().getData());
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));
  }







}
