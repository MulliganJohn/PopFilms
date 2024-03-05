const express = require('express');
const multer = require('multer');
const sharp = require('sharp');
const path = require('path');
const fs = require('fs');
const cors = require('cors');

const app = express();

app.use(cors());

async function configureMulter() {
  try {
    const storage = multer.diskStorage({
      destination: (req, file, cb) => {
        if (file.fieldname === 'bg_image_file') {
          cb(null, '../Backgrounds_Full_Res');
        } else if (file.fieldname === 'poster_image_file') {
          cb(null, '../Images_Full_Res');
        } else {
          cb(new Error('Invalid fieldname'));
        }
      },
      filename: (req, file, cb) => {
        const fileName = req.query.image_name;
        cb(null, fileName);
      }
    });

    // Configure multer upload
    const upload = multer({ storage });

    // Rest of your code involving routes and server setup
    // ...
    sharp.cache(false);
    app.post('/addMovieImages', upload.fields([
      { name: 'bg_image_file', maxCount: 1 },
      { name: 'poster_image_file', maxCount: 1 }
    ]), async (req, res) => {

      const posterThumbnailDest = path.join('../Thumbnails/', req.query.image_name);
      const posterMediumDest = path.join('../Images/', req.query.image_name);
      const backgroundMediumDest = path.join('../Backgrounds/', req.query.image_name);
      await sharp(path.join('../Images_Full_Res/', req.query.image_name))
        .resize(225, 315) // Adjust the dimensions as per your requirements
        .toFile(posterThumbnailDest)
        .then(() => {
        console.log('Image processing completed!');
      })
      .catch((error) => {
        console.error('Error during image processing:', error);
        res.status(500).send('Error processing images!');
      });

      await sharp(path.join('../Images_Full_Res/', req.query.image_name))
        .resize(300, 450) // Adjust the dimensions as per your requirements
        .toFile(posterMediumDest)
        .then(() => {
        console.log('Image processing completed!');
      })
      .catch((error) => {
        console.error('Error during image processing:', error);
        res.status(500).send('Error processing images!');
      });

      await sharp(path.join('../Backgrounds_Full_Res/', req.query.image_name))
        .resize(1366, 768) // Adjust the dimensions as per your requirements
        .toFile(backgroundMediumDest)
        .then(() => {
        console.log('Image processing completed!');
      })
      .catch((error) => {
        console.error('Error during image processing:', error);
        res.status(500).send('Error processing images!');
      });

      res.status(200).send('Files uploaded successfully!');
    });

    // GET /images/{id} endpoint
    app.get('/images/:id', (req, res) => {
      const id = req.params.id;
      const imagePath = path.join(__dirname, '../images', id);
      const isPathInExpectedDirectory = imagePath.startsWith(path.join(__dirname, '../images'));
      if (isPathInExpectedDirectory) {
          res.sendFile(imagePath);
      } 
      else {
          res.status(403).send('Forbidden');
      }
    });

    // GET /thumbnails/{id} endpoint
    app.get('/thumbnails/:id', (req, res) => {
      const id = req.params.id;
      const imagePath = path.join(__dirname, '../thumbnails', id);
      const isPathInExpectedDirectory = imagePath.startsWith(path.join(__dirname, '../thumbnails'));
      if (isPathInExpectedDirectory) {
          res.sendFile(imagePath);
      } 
      else {
          res.status(403).send('Forbidden');
      }
    });

    // GET /backgrounds/{id} endpoint
    app.get('/backgrounds/:id', (req, res) => {
      const id = req.params.id;
      const imagePath = path.join(__dirname, '../backgrounds', id);
      const isPathInExpectedDirectory = imagePath.startsWith(path.join(__dirname, '../backgrounds'));
      if (isPathInExpectedDirectory) {
          res.sendFile(imagePath);
      } 
      else {
          res.status(403).send('Forbidden');
      }
    });



    // Start the server
    app.listen(8081, () => {
      console.log('Server is running on port 8081');
    });
  } catch (error) {
    res.status(500).send('An error occurred');
    process.exit(1);
  }
}
configureMulter();
