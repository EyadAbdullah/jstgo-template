package net.jstgo.repo.template.tags;

public interface ITag {

  String openTag();

  String closeTag();

  boolean isRemovable();

}
