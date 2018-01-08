package com.fps.repository;

import com.fps.domain.Album;
import com.fps.domain.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository  extends JpaRepository<Album,Long> {

    List<Album> removeByProject(Projects projects);

    List<Album> findByProject(Projects projects);
}
