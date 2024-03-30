package pl.zielinski.SimpleLoginAndRegisterApplication.dto;

import lombok.*;

/**
 * @author rafek
 * @version 1.0
 * @licence ask rafekzielinski@wp.pl
 * @since 30/03/2024
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private String title;
    private String author;
    private String genre;
    private String publisher;
    private Long pages;

}
