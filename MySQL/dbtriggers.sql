DELIMITER //

CREATE TRIGGER after_insert_review
AFTER INSERT ON reviews FOR EACH ROW
BEGIN
    -- Update movies table
    UPDATE movies
    SET rating_sum = rating_sum + NEW.review_rating,
        total_ratings = total_ratings + 1
    WHERE id = NEW.movie_id;

    -- Update popfilmsRating in the same movies table
    UPDATE movies
    SET popfilms_rating = ROUND(rating_sum / total_ratings, 1)
    WHERE id = NEW.movie_id;
END //

CREATE TRIGGER after_update_review
AFTER UPDATE ON reviews
FOR EACH ROW
BEGIN
    UPDATE movies
    SET rating_sum = rating_sum - OLD.review_rating + NEW.review_rating
    WHERE id = NEW.movie_id;
    
    -- Update popfilmsRating in the same movies table
    UPDATE movies
    SET popfilms_rating = ROUND(rating_sum / total_ratings, 1)
    WHERE id = NEW.movie_id;
END //

CREATE TRIGGER after_delete_review
AFTER DELETE ON reviews
FOR EACH ROW
BEGIN
    UPDATE movies
    SET rating_sum = rating_sum - OLD.review_rating,
        total_ratings = total_ratings - 1
    WHERE id = OLD.movie_id;
    
	-- Update popfilmsRating in the same movies table
    UPDATE movies
    SET popfilms_rating = CASE
		WHEN total_ratings > 0 THEN ROUND(rating_sum / total_ratings, 1)
		ELSE 0 -- or another default value
	END
    WHERE id = OLD.movie_id;
END //

DELIMITER ;