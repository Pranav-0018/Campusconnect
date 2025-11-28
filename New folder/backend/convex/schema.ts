import { defineSchema, defineTable } from "convex/server";
import { v } from "convex/values";
import { authTables } from "@convex-dev/auth/server";

const applicationTables = {
  posts: defineTable({
    userId: v.id("users"),
    content: v.string(),
    imageId: v.optional(v.id("_storage")),
  }),

  events: defineTable({
    title: v.string(),
    description: v.string(),
    eventDate: v.string(), // ISO date string
    createdBy: v.id("users"),
  }).index("by_event_date", ["eventDate"]),

  queries: defineTable({
    studentId: v.id("users"),
    question: v.string(),
    answer: v.optional(v.string()),
    answeredBy: v.optional(v.id("users")),
    answeredAt: v.optional(v.number()),
  }),

  profiles: defineTable({
    userId: v.id("users"),
    role: v.union(v.literal("student"), v.literal("teacher")),
    class: v.optional(v.string()),
    section: v.optional(v.string()),
    bio: v.optional(v.string()),
  }).index("by_user", ["userId"]),
};

export default defineSchema({
  ...authTables,
  ...applicationTables,
});
