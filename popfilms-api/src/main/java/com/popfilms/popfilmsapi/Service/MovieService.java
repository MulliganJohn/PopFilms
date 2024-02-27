package com.popfilms.popfilmsapi.Service;

import com.popfilms.popfilmsapi.Entity.GenreEntity;
import com.popfilms.popfilmsapi.Entity.MovieEntity;
import com.popfilms.popfilmsapi.Entity.StudioEntity;
import com.popfilms.popfilmsapi.Entity.DirectorEntity;
import com.popfilms.popfilmsapi.Entity.WriterEntity;
import com.popfilms.popfilmsapi.Repository.*;
import com.popfilms.popfilmsapi.dtos.MovieDto;
import com.popfilms.popfilmsapi.dtos.ReviewRequestDto;
import com.popfilms.popfilmsapi.dtos.SearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import com.popfilms.popfilmsapi.util.ImageServerUploader;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MovieService {


    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private GenreRepo genreRepo;
    @Autowired
    private StudioRepo studioRepo;
    @Autowired
    private DirectorRepo directorRepo;
    @Autowired
    private WriterRepo writerRepo;


    @Transactional
    public MovieEntity addMovie(MovieDto movieDto, MultipartFile poster_image, MultipartFile bg_image){
        try {
            for (Field field : movieDto.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (ObjectUtils.isEmpty(field.get(movieDto))) {
                    throw new RuntimeException("Missing or empty " + field.getName() + " field.");
                }
            }
            MovieEntity newMovie = new MovieEntity(movieDto.getTitle(), movieDto.getReleaseYear(), movieDto.getLength(), movieDto.getRating(), movieDto.getPopfilmsRating(), movieDto.getTmdbRating(), movieDto.getSummary());
            newMovie.setRatingSum(0);
            newMovie.setTotalRatings(0);

            for (String genreName : movieDto.getGenres()) {
                Optional<GenreEntity> checkGenre = genreRepo.findByName(genreName);
                GenreEntity genre = new GenreEntity();
                if (checkGenre.isEmpty()) {
                    genre.setName(genreName);
                    genreRepo.save(genre);
                    newMovie.addGenre(genre);
                }
                else {newMovie.addGenre(checkGenre.get());}
            }

            for (String studioName : movieDto.getStudios()) {
                Optional<StudioEntity> checkStudio = studioRepo.findByName(studioName);
                StudioEntity studio = new StudioEntity();
                if (checkStudio.isEmpty()) {
                    studio.setName(studioName);
                    studioRepo.save(studio);
                    newMovie.addStudio(studio);
                }
                else {newMovie.addStudio(checkStudio.get());}
            }

            for (String directorName : movieDto.getDirectors()) {
                Optional<DirectorEntity> checkDirector = directorRepo.findByName(directorName);
                DirectorEntity director = new DirectorEntity();
                if (checkDirector.isEmpty()) {
                    director.setName(directorName);
                    directorRepo.save(director);
                    newMovie.addDirector(director);
                }
                else {newMovie.addDirector(checkDirector.get());}
            }

            for (String writerName : movieDto.getWriters()) {
                Optional<WriterEntity> checkWriter = writerRepo.findByName(writerName);
                WriterEntity writer = new WriterEntity();
                if (checkWriter.isEmpty()) {
                    writer.setName(writerName);
                    writerRepo.save(writer);
                    newMovie.addWriter(writer);
                }
                else {newMovie.addWriter(checkWriter.get());}
            }

            System.out.println("about to save movie");
            newMovie = movieRepo.save(newMovie);
            System.out.println("saved movie");


            Resource poster_imageResource = poster_image.getResource();
            Resource bg_imageResource = bg_image.getResource();
            String imageName = ImageServerUploader.getImageName(poster_imageResource.getFilename(), newMovie.getId());
            String imageUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/addMovieImages").queryParam("image_name", imageName).toUriString();
            ImageServerUploader.sendPostRequest(imageUrl, poster_imageResource, bg_imageResource);

            return newMovie;

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public MovieEntity getMovieById(Long id) {
        try{
            Optional<MovieEntity> optionalMovie = movieRepo.findById(id);
            if (optionalMovie.isPresent()) {
                return optionalMovie.get();
            } else {
                throw new NoSuchElementException("Movie not found with id " + id);
            }
        } catch (Exception e){
            throw new RuntimeException("Error occurred while retrieving entity with id " + id, e);
        }
    }

    public List<Long> getTop30MovieIds(){
        return movieRepo.findTop30Ids();
    }

    public List<Long> get30RandomIds(){
        return movieRepo.find30RandomIds();
    }

    public List<SearchResultDto> getMoviesBySearch(String query){
        return movieRepo.getMoviesBySearch(query, PageRequest.of(0, 10));
    }
}
