package com.github.jankwq.yalta.util.poster.cover;

import lombok.NoArgsConstructor;

import java.awt.*;

/**
 * @author yinjianfeng
 * @date 2018/11/14
 */
@NoArgsConstructor
public class TextCover extends AbstractCover {

    private String text;

    private Font font;

    private Color color;

    public TextCover(String text){
        setText(text);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if ( font != null ){
            graphics2D.setFont(font);
        }
        if ( color != null ){
            graphics2D.setColor(color);
        }
        graphics2D.drawString(text, x, y);
    }

    public TextCover setText(String text){
        this.text = text;
        return this;
    }

    public TextCover setFont(Font font) {
        this.font = font;
        return this;
    }

    public TextCover setColor(Color color) {
        this.color = color;
        return this;
    }
}
