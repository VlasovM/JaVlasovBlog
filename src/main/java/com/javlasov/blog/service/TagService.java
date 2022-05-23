package com.javlasov.blog.service;

import com.javlasov.blog.api.response.TagResponse;
import com.javlasov.blog.dto.TagDto;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.model.Tag;
import com.javlasov.blog.model.Tag2Post;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.Tag2PostRepository;
import com.javlasov.blog.repository.TagRepository;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final DtoMapper dtoMapper;

    private final TagRepository tagRepository;

    private final PostRepository postRepository;

    private final Tag2PostRepository tag2PostRepository;

    public TagResponse tag(Optional<String> query) {
        TagResponse tagResponse = new TagResponse();
        if (!checkTag()) {
            return tagResponse;
        }
        List<Tag> tagList = findTagsWithQuery(query);
        List<TagDto> tagDtoList = prepareTag(tagList);
        tagResponse.setTags(tagDtoList);
        return tagResponse;
    }

    private boolean checkTag() {
        if (postRepository.findAll().isEmpty()) {
            return false;
        }
        return !tag2PostRepository.findAll().isEmpty();
    }

    private List<TagDto> prepareTag(List<Tag> tagList) {
        List<TagDto> result = new ArrayList<>();
        Tag mostPopularTag = findMostPopularTag(tagList);
        double coefficientK = calculateKCoefficient(mostPopularTag);
        for (Tag tag : tagList) {
            TagDto tagDto = dtoMapper.tagToTagDto(tag);
            tagDto.setWeight(calculateTagWeight(tag, coefficientK));
            result.add(tagDto);
        }
        return result;
    }

    private List<Tag> findTagsWithQuery(Optional<String> query) {
        if (query.isEmpty()) {
            return tagRepository.findAll();
        }
        return tagRepository.findAll().stream()
                .filter(tag -> StringUtils.startsWithIgnoreCase(tag.getName(), query.get()))
                .collect(Collectors.toList());
    }

    private Tag findMostPopularTag(List<Tag> tagList) {
        Tag mostPopularTag = new Tag();
        int count = 0;
        for (Tag tag : tagList) {
            if (tag.getPosts().size() > count) {
                mostPopularTag = tag;
                count = tag.getPosts().size();
            }
        }
        return mostPopularTag;
    }

    private double calculateTagWeight(Tag tag, double coefficientK) {
        double dWeightTag = (double) tag.getPosts().size() / postRepository.findAll().size();
        return dWeightTag * coefficientK;
    }

    private double calculateKCoefficient(Tag mostPopularTag) {
        double dWeightMax = (double) mostPopularTag.getPosts().size() / postRepository.findAll().size();
        return 1 / dWeightMax;
    }

}
