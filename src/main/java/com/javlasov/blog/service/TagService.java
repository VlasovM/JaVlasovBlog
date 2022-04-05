package com.javlasov.blog.service;

import com.javlasov.blog.api.response.TagResponse;
import com.javlasov.blog.dto.TagDto;
import com.javlasov.blog.entity.Tag;
import com.javlasov.blog.mappers.DtoMapper;
import com.javlasov.blog.repository.PostRepository;
import com.javlasov.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TagService {

    private final DtoMapper dtoMapper;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private List<Tag> tagList;
    private double coefficientK;
    private double allPosts;

    public TagResponse tag(String query) {
        TagResponse tagResponse = new TagResponse();
        tagList = findTagsWithQuery(query);
        allPosts = postRepository.findAll().size();
        List<TagDto> tagDtoList = dtoMapper.TagDtoToTagDtoList(tagList);

        calculateKCoefficient(findMostPopularTag());

        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i);
            double weight = calculateTagWeight(tag);
            tagDtoList.get(i).setWeight(weight);
        }

        tagResponse.setTags(tagDtoList);
        return tagResponse;
    }

    private List<Tag> findTagsWithQuery(String query) {
        if (query == null) {
            return tagRepository.findAll();
        } else {
            List<Tag> allTags = tagRepository.findAll();
            tagList = new ArrayList<>();
            for (Tag tag : allTags) {
                if (tag.getName().startsWith(query) || tag.getName().startsWith(query.toLowerCase(Locale.ROOT))) {
                    tagList.add(tag);
                }
            }
            return tagList;
        }
    }

    private double calculateTagWeight(Tag tag) {
        double dWeightTag = tag.getPosts().size() / allPosts;
        return dWeightTag * coefficientK;
    }

    private Tag findMostPopularTag() {
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

    private void calculateKCoefficient(Tag mostPopularTag) {
        double dWeightMax = mostPopularTag.getPosts().size() / allPosts;
        coefficientK = 1 / dWeightMax;
    }
}
