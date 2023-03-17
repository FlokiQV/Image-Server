package pdl.backend;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import javax.imageio.ImageIO;


@Repository
public class ImageDao implements Dao<Image> {

  private final Map<Long, Image> images = new HashMap<>();

  public ImageDao() {
   // final ClassPathResource imgFolder = new ClassPathResource("/images");
    try {
      Path baseDirectory = Paths.get("");
      Path imagesDirectory = findImagesDirectory(baseDirectory);
  
      if (imagesDirectory != null) {
        File[] files = imagesDirectory.toFile().listFiles();
        for (File file : files) {
          if (file.isFile() &&file.getName().endsWith(".jpg") || (file.getName().endsWith(".jpeg") ||  file.getName().endsWith(".png"))) {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            Image img = new Image(file.getName(), fileContent);
            images.put(img.getId(), img);
          }
        }
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
  private Path findImagesDirectory(Path directory) throws IOException {
    if (Files.exists(directory) && Files.isDirectory(directory)) {
      Path imagesDirectory = directory.resolve("images");
      if (Files.exists(imagesDirectory) && Files.isDirectory(imagesDirectory)) {
        return imagesDirectory;
      } else {
        try (DirectoryStream<Path> subDirectories = Files.newDirectoryStream(directory)) {
          for (Path subDirectory : subDirectories) {
            Path imagesSubDirectory = findImagesDirectory(subDirectory);
            if (imagesSubDirectory != null) {
              return imagesSubDirectory;
            }
          }
        }
      }
    }
    return null;}

  @Override
  public Optional<Image> retrieve(final long id) {
    return Optional.ofNullable(images.get(id));
  }

  @Override
  public List<Image> retrieveAll() {
    return new ArrayList<Image>(images.values());
  }

  @Override
  public void create(final Image img) {
    images.put(img.getId(), img);
  }

  @Override
  public void update(final Image img, final String[] params) {
    img.setName(Objects.requireNonNull(params[0], "Name cannot be null"));

    images.put(img.getId(), img);
  }

  @Override
  public void delete(final Image img) {
    images.remove(img.getId());
  }

  // Une méthode pour faire tourner l'algorithm sur une image dans la classe ImageController
  public Optional<Image> processing(final EnumProcessing Algo, final Image img, final float[] options) throws Exception {
    InputStream is = new ByteArrayInputStream(img.getData());
    BufferedImage buffImage = ImageIO.read(is);
    Planar<GrayU8> input = ConvertBufferedImage.convertFromPlanar(buffImage, null, true, GrayU8.class);
    Planar<GrayU8> output = input.createSameShape();
    switch(Algo){
      case LUMI:
        ImagesAlgorithmes.lumirgb(input, output, (int)options[0]);
        break;
      case COLOR:
        ImagesAlgorithmes.teinteImage(input, output, options[0]);
        break;
      case MeanFilterColor:
        ImagesAlgorithmes.MeanFilterColor(input, output, (int)options[0]);
        break;
      case FLOUGAUSS:
      ImagesAlgorithmes.GaussFilterRGB(input, output);
        break;
      case HISTO:
        ImagesAlgorithmes.historgb(input, output);
        break;
      case SOBEL:
        ImagesAlgorithmes.gradientImageSobelRGB(input, output);
        break;
    }
    ConvertBufferedImage.convertTo_U8(output, buffImage, true);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write(buffImage, "png", out);
    byte[] outputBytes = out.toByteArray();
    Image outputImage = new Image(img.getName(), outputBytes);

    return Optional.ofNullable(outputImage);//faire transformation du output en image
  }
}
