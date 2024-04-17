package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingViewDto;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.exception.NotOwnerForbiddenException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemViewDto> getAllItemsByOwner(Long id) {
        Map<Long, Item> itemMap = repository.findAllByOwnerId(id)
                .stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        Map<Long, List<BookingViewDto>> bookingMap = bookingRepository.findByItemIdIn(itemMap.keySet())
                .stream()
                .map(BookingMapper::toBookingViewDto)
                .collect(Collectors.groupingBy(BookingViewDto::getItemId));

        Map<Long, List<CommentDto>> commentMap = commentRepository.findByItemIdIn(itemMap.keySet())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        return itemMap.values()
                .stream()
                .map(item -> ItemMapper.toItemViewForOwnerDto(item,
                        bookingMap.getOrDefault(item.getId(), Collections.emptyList()),
                        commentMap.getOrDefault(item.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long id) {
        UserDto userDto = userService.findUserById(id);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(UserMapper.toUser(userDto));
        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        UserDto userDto = userService.findUserById(userId);
        Item itemToUpdate = itemStorage.findItemById(itemId);

        if (!itemToUpdate.getOwner().getId().equals(userId)) {
            throw new NotOwnerForbiddenException("User is not the owner of an item");
        }

        boolean updated = false;
        if (itemDto.getName() != null) {
            itemToUpdate.setName(itemDto.getName());
            updated = true;
        }
        if (itemDto.getAvailable() != null) {
            itemToUpdate.setAvailable(itemDto.getAvailable());
            updated = true;
        }
        if (itemDto.getDescription() != null) {
            itemToUpdate.setDescription(itemDto.getDescription());
            updated = true;
        }
        if (updated) {
            return ItemMapper.toItemDto(itemToUpdate);
        }
        log.warn("update of item with id {} failed", itemId);
        throw new ItemValidationException("Unable to update empty parameters of item");
    }

    @Override
    public ItemDto findItemById(Long id) {
        return ItemMapper.toItemDto(itemStorage.findItemById(id));
    }

    @Override
    public void delete(Long id) {
        itemStorage.delete(id);
    }

    @Override
    public List<ItemDto> searchItemByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return ItemMapper.mapToItemDto(itemStorage.searchItemByText(text));
    }
}
