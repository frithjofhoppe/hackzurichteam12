package com.hackzurich.hackzurichteam12.backend.service;

import com.hackzurich.hackzurichteam12.backend.api.LocationRecognitionResult;
import com.hackzurich.hackzurichteam12.backend.api.LocationRecognizerService;
import com.hackzurich.hackzurichteam12.backend.model.RawNewsArticle;
import com.hackzurich.hackzurichteam12.backend.model.entity.LocationEntity;
import com.hackzurich.hackzurichteam12.backend.model.entity.NewsArticle;
import com.hackzurich.hackzurichteam12.backend.model.repo.LocationRepository;
import com.hackzurich.hackzurichteam12.backend.model.repo.NewsArticleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class NewsArticleService {
    private LocationRecognizerService locationRecognizerService;
    private NewsArticleRepository articleRepository;
    private LocationRepository locationRepository;

    public List<NewsArticle> filterEnrichAndPerist(Stream<RawNewsArticle> articleStream) {
        final List<NewsArticle> newsArticles = articleStream.filter(RawNewsArticle::containsCorona).map(rawNewsArticle -> {
            final LocationRecognitionResult location =
                    locationRecognizerService.findCityCoordinatesByMessage(rawNewsArticle.getTextStructureJson());
            LocationEntity locationEntity = null;
            if(locationRepository.existsByLongitudeAndLatitude(location.getCoordinates().getLongitude(),location.getCoordinates().getLatitude())){
                locationEntity = locationRepository.findFirstByLongitudeAndLatitude(location.getCoordinates().getLongitude(),location.getCoordinates().getLatitude());
            } else {
                locationEntity = new LocationEntity(null, location.getCity(), location.getCoordinates().getLongitude(), location.getCoordinates().getLatitude());
                locationEntity = locationRepository.saveAndFlush(locationEntity);
            }
            final NewsArticle article = new NewsArticle(null, rawNewsArticle.getSourceText(),
                    rawNewsArticle.getPublicationDate(),
                    rawNewsArticle.getTextStructureJson(),
                    rawNewsArticle.getLanguage(),
                    locationEntity);

            return articleRepository.save(article);
        }).collect(Collectors.toList());
        articleRepository.flush();
        return newsArticles;
    }


}
