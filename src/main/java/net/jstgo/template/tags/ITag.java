package net.jstgo.template.tags;

public interface ITag {

  String openTag();

  String closeTag();

  boolean isRemovable();

}
