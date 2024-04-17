package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemViewDto;

import java.util.List;

public interface ItemService {

    List<ItemViewDto > getAllItemsByOwner(Long id);

    ItemDto create(ItemDto itemDto, Long id);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    ItemViewDto findItemById(Long id);

    void delete(Long id);

    List<ItemDto> searchItemByText(String text);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long userId);
}
