package com.noteapp.domain.usecase;

import com.noteapp.domain.repository.NoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SearchNotesUseCase_Factory implements Factory<SearchNotesUseCase> {
  private final Provider<NoteRepository> repositoryProvider;

  public SearchNotesUseCase_Factory(Provider<NoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchNotesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchNotesUseCase_Factory create(Provider<NoteRepository> repositoryProvider) {
    return new SearchNotesUseCase_Factory(repositoryProvider);
  }

  public static SearchNotesUseCase newInstance(NoteRepository repository) {
    return new SearchNotesUseCase(repository);
  }
}
