package org.example.individual3.dto;

import org.example.individual3.entity.BookStatus;

public record BookStatusRequest(
        BookStatus status
) {}